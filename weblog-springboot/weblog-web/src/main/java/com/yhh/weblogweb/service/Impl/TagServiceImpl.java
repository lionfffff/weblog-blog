package com.yhh.weblogweb.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhh.weblogcommon.domain.dos.ArticleDO;
import com.yhh.weblogcommon.domain.dos.ArticleTagRelDO;
import com.yhh.weblogcommon.domain.dos.TagDO;
import com.yhh.weblogcommon.domain.mapper.ArticleMapper;
import com.yhh.weblogcommon.domain.mapper.ArticleTagRelMapper;
import com.yhh.weblogcommon.domain.mapper.TagMapper;
import com.yhh.weblogcommon.enums.ResponseCodeEnum;
import com.yhh.weblogcommon.exception.BizException;
import com.yhh.weblogcommon.utils.PageResponse;
import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.convert.ArticleConvert;
import com.yhh.weblogweb.model.vo.tag.FindTagArticlePageListReqVO;
import com.yhh.weblogweb.model.vo.tag.FindTagArticlePageListRspVO;
import com.yhh.weblogweb.model.vo.tag.FindTagListReqVO;
import com.yhh.weblogweb.model.vo.tag.FindTagListRspVO;
import com.yhh.weblogweb.service.BlogOwnerService;
import com.yhh.weblogweb.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private BlogOwnerService blogOwnerService;

    @Override
    public Response findTagList(FindTagListReqVO findTagListReqVO) {
        Long ownerId = blogOwnerService.getOwnerIdByUsername(findTagListReqVO.getBlogUsername());
        List<TagDO> tagDOS = tagMapper.selectByOwnerId(ownerId);

        List<FindTagListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(tagDOS)) {
            vos = tagDOS.stream()
                    .map(tagDO -> FindTagListRspVO.builder()
                            .id(tagDO.getId())
                            .name(tagDO.getName())
                            .articlesTotal(tagDO.getArticlesTotal())
                            .build())
                    .collect(Collectors.toList());
        }
        return Response.success(vos);
    }

    @Override
    public Response findTagPageList(FindTagArticlePageListReqVO findTagArticlePageListReqVO) {
        Long ownerId = blogOwnerService.getOwnerIdByUsername(findTagArticlePageListReqVO.getBlogUsername());
        Long tagId = findTagArticlePageListReqVO.getId();

        TagDO tagDO = tagMapper.selectById(tagId);
        if (tagDO == null || !Objects.equals(tagDO.getOwnerId(), ownerId)) {
            throw new BizException(ResponseCodeEnum.TAG_NOT_EXISTED);
        }

        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByTagId(tagId);
        if (CollectionUtils.isEmpty(articleTagRelDOS)) {
            return PageResponse.success(null, null);
        }

        List<Long> articleIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getArticleId).collect(Collectors.toList());
        Page<ArticleDO> page = articleMapper.selectPageListByArticleIds(
                findTagArticlePageListReqVO.getCurrent(),
                findTagArticlePageListReqVO.getSize(),
                articleIds,
                ownerId);

        List<FindTagArticlePageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            vos = page.getRecords().stream()
                    .map(ArticleConvert.INSTANCE::convertDO2TagArticleVO)
                    .collect(Collectors.toList());
        }
        return PageResponse.success(page, vos);
    }
}
