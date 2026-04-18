package com.yhh.weblogweb.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yhh.weblogcommon.constant.Constants;
import com.yhh.weblogcommon.domain.dos.ArticleDO;
import com.yhh.weblogcommon.domain.dos.CategoryDO;
import com.yhh.weblogcommon.domain.dos.StatisticsArticlePVDO;
import com.yhh.weblogcommon.domain.dos.TagDO;
import com.yhh.weblogcommon.domain.mapper.ArticleMapper;
import com.yhh.weblogcommon.domain.mapper.CategoryMapper;
import com.yhh.weblogcommon.domain.mapper.StatisticsArticlePVMapper;
import com.yhh.weblogcommon.domain.mapper.TagMapper;
import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.model.vo.statistics.FindStatisticsInfoReqVO;
import com.yhh.weblogweb.model.vo.statistics.FindStatisticsInfoRspVO;
import com.yhh.weblogweb.model.vo.statistics.FindStatisticsPVTrendRspVO;
import com.yhh.weblogweb.service.BlogOwnerService;
import com.yhh.weblogweb.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private StatisticsArticlePVMapper statisticsArticlePVMapper;

    @Autowired
    private BlogOwnerService blogOwnerService;

    @Override
    public Response findInfo(FindStatisticsInfoReqVO findStatisticsInfoReqVO) {
        Long ownerId = blogOwnerService.getOwnerIdByUsername(findStatisticsInfoReqVO.getBlogUsername());
        Long articleTotalCount = articleMapper.selectCount(Wrappers.<ArticleDO>lambdaQuery().eq(ArticleDO::getOwnerId, ownerId));
        Long categoryTotalCount = categoryMapper.selectCount(Wrappers.<CategoryDO>lambdaQuery().eq(CategoryDO::getOwnerId, ownerId));
        Long tagTotalCount = tagMapper.selectCount(Wrappers.<TagDO>lambdaQuery().eq(TagDO::getOwnerId, ownerId));

        List<ArticleDO> articleDOS = articleMapper.selectAllReadNum(ownerId);
        Long pvTotalCount = 0L;
        if (!CollectionUtils.isEmpty(articleDOS)) {
            pvTotalCount = articleDOS.stream().mapToLong(ArticleDO::getReadNum).sum();
        }

        return Response.success(FindStatisticsInfoRspVO.builder()
                .articleTotalCount(articleTotalCount)
                .categoryTotalCount(categoryTotalCount)
                .tagTotalCount(tagTotalCount)
                .pvTotalCount(pvTotalCount)
                .build());
    }

    @Override
    public Response findPVTrend(FindStatisticsInfoReqVO findStatisticsInfoReqVO) {
        Long ownerId = blogOwnerService.getOwnerIdByUsername(findStatisticsInfoReqVO.getBlogUsername());

        List<ArticleDO> articleDOS = articleMapper.selectAllReadNum(ownerId);
        Long pvTotalCount = 0L;
        if (!CollectionUtils.isEmpty(articleDOS)) {
            pvTotalCount = articleDOS.stream().mapToLong(ArticleDO::getReadNum).sum();
        }

        LocalDate currDate = LocalDate.now();
        LocalDate startDate = currDate.minusDays(6);
        List<StatisticsArticlePVDO> statisticsArticlePVDOS = statisticsArticlePVMapper.selectList(
                Wrappers.<StatisticsArticlePVDO>lambdaQuery()
                        .eq(StatisticsArticlePVDO::getOwnerId, ownerId)
                        .between(StatisticsArticlePVDO::getPvDate, startDate, currDate)
                        .orderByAsc(StatisticsArticlePVDO::getPvDate)
        );

        Map<LocalDate, Long> pvDateCountMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(statisticsArticlePVDOS)) {
            pvDateCountMap = statisticsArticlePVDOS.stream()
                    .collect(Collectors.toMap(StatisticsArticlePVDO::getPvDate, StatisticsArticlePVDO::getPvCount));
        }

        List<String> pvDates = Lists.newArrayList();
        List<Long> pvCounts = Lists.newArrayList();
        for (LocalDate date = startDate; !date.isAfter(currDate); date = date.plusDays(1)) {
            pvDates.add(date.format(Constants.MONTH_DAY_FORMATTER));
            pvCounts.add(pvDateCountMap.getOrDefault(date, 0L));
        }

        return Response.success(FindStatisticsPVTrendRspVO.builder()
                .pvDates(pvDates)
                .pvCounts(pvCounts)
                .pvTotalCount(pvTotalCount)
                .build());
    }
}
