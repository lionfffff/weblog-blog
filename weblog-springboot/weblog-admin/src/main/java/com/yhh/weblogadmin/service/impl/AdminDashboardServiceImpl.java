package com.yhh.weblogadmin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yhh.weblogadmin.model.vo.dashboard.FindDashboardPVStatisticsInfoRspVO;
import com.yhh.weblogadmin.model.vo.dashboard.FindDashboardStatisticsInfoRspVO;
import com.yhh.weblogadmin.service.AdminDashboardService;
import com.yhh.weblogadmin.service.AdminUserContextService;
import com.yhh.weblogcommon.constant.Constants;
import com.yhh.weblogcommon.domain.dos.ArticleDO;
import com.yhh.weblogcommon.domain.dos.ArticlePublishCountDO;
import com.yhh.weblogcommon.domain.dos.StatisticsArticlePVDO;
import com.yhh.weblogcommon.domain.mapper.ArticleMapper;
import com.yhh.weblogcommon.domain.mapper.CategoryMapper;
import com.yhh.weblogcommon.domain.mapper.StatisticsArticlePVMapper;
import com.yhh.weblogcommon.domain.mapper.TagMapper;
import com.yhh.weblogcommon.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private StatisticsArticlePVMapper articlePVMapper;

    @Autowired
    private AdminUserContextService adminUserContextService;

    @Override
    public Response findDashboardStatistics() {
        Long ownerId = adminUserContextService.getUserId();
        Long articleTotalCount = articleMapper.selectCount(Wrappers.<ArticleDO>lambdaQuery().eq(ArticleDO::getOwnerId, ownerId));
        Long categoryTotalCount = categoryMapper.selectCount(Wrappers.<com.yhh.weblogcommon.domain.dos.CategoryDO>lambdaQuery().eq(com.yhh.weblogcommon.domain.dos.CategoryDO::getOwnerId, ownerId));
        Long tagTotalCount = tagMapper.selectCount(Wrappers.<com.yhh.weblogcommon.domain.dos.TagDO>lambdaQuery().eq(com.yhh.weblogcommon.domain.dos.TagDO::getOwnerId, ownerId));

        List<ArticleDO> articleDOS = articleMapper.selectAllReadNum(ownerId);
        Long pvTotalCount = 0L;
        if (!CollectionUtils.isEmpty(articleDOS)) {
            pvTotalCount = articleDOS.stream().mapToLong(ArticleDO::getReadNum).sum();
        }

        return Response.success(FindDashboardStatisticsInfoRspVO.builder()
                .articleTotalCount(articleTotalCount)
                .categoryTotalCount(categoryTotalCount)
                .tagTotalCount(tagTotalCount)
                .pvTotalCount(pvTotalCount)
                .build());
    }

    @Override
    public Response findDashboardPublishArticleStatistics() {
        Long ownerId = adminUserContextService.getUserId();
        LocalDate currDate = LocalDate.now();
        LocalDate startDate = currDate.minusYears(1);
        List<ArticlePublishCountDO> articlePublishCountDOS = articleMapper.selectDateArticlePublishCountByOwner(startDate, currDate.plusDays(1), ownerId);

        Map<LocalDate, Long> map = Maps.newLinkedHashMap();
        Map<LocalDate, Long> dateArticleCountMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(articlePublishCountDOS)) {
            dateArticleCountMap = articlePublishCountDOS.stream()
                    .collect(Collectors.toMap(ArticlePublishCountDO::getDate, ArticlePublishCountDO::getCount));
        }
        for (LocalDate date = startDate; date.isBefore(currDate) || date.isEqual(currDate); date = date.plusDays(1)) {
            map.put(date, dateArticleCountMap.getOrDefault(date, 0L));
        }
        return Response.success(map);
    }

    @Override
    public Response findDashboardPVStatistics() {
        Long ownerId = adminUserContextService.getUserId();
        List<StatisticsArticlePVDO> articlePVDOS = articlePVMapper.selectLatestWeekRecords(ownerId);

        Map<LocalDate, Long> pvDateCountMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(articlePVDOS)) {
            pvDateCountMap = articlePVDOS.stream()
                    .collect(Collectors.toMap(StatisticsArticlePVDO::getPvDate, StatisticsArticlePVDO::getPvCount));
        }

        List<String> pvDates = Lists.newArrayList();
        List<Long> pvCounts = Lists.newArrayList();
        LocalDate currDate = LocalDate.now();
        for (LocalDate date = currDate.minusWeeks(1); date.isBefore(currDate) || date.isEqual(currDate); date = date.plusDays(1)) {
            pvDates.add(date.format(Constants.MONTH_DAY_FORMATTER));
            Long pvCount = pvDateCountMap.get(date);
            pvCounts.add(pvCount == null ? 0L : pvCount);
        }

        return Response.success(FindDashboardPVStatisticsInfoRspVO.builder()
                .pvDates(pvDates)
                .pvCounts(pvCounts)
                .build());
    }
}
