package com.yhh.weblogweb.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.yhh.weblogcommon.domain.dos.ArticleDO;
import com.yhh.weblogcommon.domain.mapper.ArticleMapper;
import com.yhh.weblogcommon.utils.PageResponse;
import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.convert.ArticleConvert;
import com.yhh.weblogweb.model.vo.archive.FindArchiveArticlePageListReqVO;
import com.yhh.weblogweb.model.vo.archive.FindArchiveArticlePageListRspVO;
import com.yhh.weblogweb.model.vo.archive.FindArchiveArticleRspVO;
import com.yhh.weblogweb.service.ArchiveService;
import com.yhh.weblogweb.service.BlogOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ArchiveServiceImpl implements ArchiveService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private BlogOwnerService blogOwnerService;

    @Override
    public Response findArchivePageList(FindArchiveArticlePageListReqVO findArchiveArticlePageListReqVO) {
        Long ownerId = blogOwnerService.getOwnerIdByUsername(findArchiveArticlePageListReqVO.getBlogUsername());
        IPage<ArticleDO> page = articleMapper.selectPageList(
                findArchiveArticlePageListReqVO.getCurrent(),
                findArchiveArticlePageListReqVO.getSize(),
                null,
                null,
                null,
                ownerId);
        List<ArticleDO> articleDOS = page.getRecords();

        List<FindArchiveArticlePageListRspVO> vos = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(articleDOS)) {
            List<FindArchiveArticleRspVO> archiveArticleRspVOS = articleDOS.stream()
                    .map(ArticleConvert.INSTANCE::convertDO2ArchiveArticleVO)
                    .collect(Collectors.toList());

            Map<YearMonth, List<FindArchiveArticleRspVO>> map = archiveArticleRspVOS.stream()
                    .collect(Collectors.groupingBy(FindArchiveArticleRspVO::getCreateMonth));
            Map<YearMonth, List<FindArchiveArticleRspVO>> sortedMap = new TreeMap<>(Collections.reverseOrder());
            sortedMap.putAll(map);
            sortedMap.forEach((k, v) -> vos.add(FindArchiveArticlePageListRspVO.builder().month(k).articles(v).build()));
        }

        return PageResponse.success(page, vos);
    }
}
