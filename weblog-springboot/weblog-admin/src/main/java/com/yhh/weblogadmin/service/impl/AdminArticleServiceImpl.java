package com.yhh.weblogadmin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yhh.weblogadmin.convert.ArticleDetailConvert;
import com.yhh.weblogadmin.event.DeleteArticleEvent;
import com.yhh.weblogadmin.event.PublishArticleEvent;
import com.yhh.weblogadmin.event.UpdateArticleEvent;
import com.yhh.weblogadmin.model.vo.article.DeleteArticleReqVO;
import com.yhh.weblogadmin.model.vo.article.FindArticleDetailReqVO;
import com.yhh.weblogadmin.model.vo.article.FindArticleDetailRspVO;
import com.yhh.weblogadmin.model.vo.article.FindArticlePageListReqVO;
import com.yhh.weblogadmin.model.vo.article.FindArticlePageListRspVO;
import com.yhh.weblogadmin.model.vo.article.PublishArticleReqVO;
import com.yhh.weblogadmin.model.vo.article.UpdateArticleReqVO;
import com.yhh.weblogadmin.service.AdminArticleService;
import com.yhh.weblogadmin.service.AdminUserContextService;
import com.yhh.weblogcommon.domain.dos.ArticleCategoryRelDO;
import com.yhh.weblogcommon.domain.dos.ArticleContentDO;
import com.yhh.weblogcommon.domain.dos.ArticleDO;
import com.yhh.weblogcommon.domain.dos.ArticleTagRelDO;
import com.yhh.weblogcommon.domain.dos.CategoryDO;
import com.yhh.weblogcommon.domain.dos.TagDO;
import com.yhh.weblogcommon.domain.mapper.ArticleCategoryRelMapper;
import com.yhh.weblogcommon.domain.mapper.ArticleContentMapper;
import com.yhh.weblogcommon.domain.mapper.ArticleMapper;
import com.yhh.weblogcommon.domain.mapper.ArticleTagRelMapper;
import com.yhh.weblogcommon.domain.mapper.CategoryMapper;
import com.yhh.weblogcommon.domain.mapper.TagMapper;
import com.yhh.weblogcommon.enums.ResponseCodeEnum;
import com.yhh.weblogcommon.exception.BizException;
import com.yhh.weblogcommon.utils.PageResponse;
import com.yhh.weblogcommon.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminArticleServiceImpl implements AdminArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleContentMapper articleContentMapper;

    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private AdminUserContextService adminUserContextService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response publishArticle(PublishArticleReqVO publishArticleReqVO) {
        Long ownerId = adminUserContextService.getUserId();
        CategoryDO categoryDO = getOwnedCategory(publishArticleReqVO.getCategoryId(), ownerId);

        ArticleDO articleDO = ArticleDO.builder()
                .title(publishArticleReqVO.getTitle())
                .cover(publishArticleReqVO.getCover())
                .summary(publishArticleReqVO.getSummary())
                .ownerId(ownerId)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        articleMapper.insert(articleDO);

        Long articleId = articleDO.getId();
        articleContentMapper.insert(ArticleContentDO.builder()
                .articleId(articleId)
                .content(publishArticleReqVO.getContent())
                .build());

        articleCategoryRelMapper.insert(ArticleCategoryRelDO.builder()
                .articleId(articleId)
                .categoryId(categoryDO.getId())
                .build());

        insertTags(articleId, publishArticleReqVO.getTags(), ownerId);
        eventPublisher.publishEvent(new PublishArticleEvent(this, articleId));
        return Response.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response deleteArticle(DeleteArticleReqVO deleteArticleReqVO) {
        Long ownerId = adminUserContextService.getUserId();
        Long articleId = deleteArticleReqVO.getId();
        getOwnedArticle(articleId, ownerId);

        articleMapper.deleteById(articleId);
        articleContentMapper.deleteByArticleId(articleId);
        articleCategoryRelMapper.deleteByArticleId(articleId);
        articleTagRelMapper.deleteByArticleId(articleId);
        eventPublisher.publishEvent(new DeleteArticleEvent(this, articleId));
        return Response.success();
    }

    @Override
    public Response findArticlePageList(FindArticlePageListReqVO findArticlePageListReqVO) {
        Long ownerId = adminUserContextService.getUserId();
        Long current = findArticlePageListReqVO.getCurrent();
        Long size = findArticlePageListReqVO.getSize();
        String title = findArticlePageListReqVO.getTitle();
        LocalDate startDate = findArticlePageListReqVO.getStartDate();
        LocalDate endDate = findArticlePageListReqVO.getEndDate();

        Page<ArticleDO> articleDOPage = articleMapper.selectPageList(current, size, title, startDate, endDate, ownerId);
        List<FindArticlePageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(articleDOPage.getRecords())) {
            vos = articleDOPage.getRecords().stream()
                    .map(articleDO -> FindArticlePageListRspVO.builder()
                            .id(articleDO.getId())
                            .title(articleDO.getTitle())
                            .cover(articleDO.getCover())
                            .createTime(articleDO.getCreateTime())
                            .build())
                    .collect(Collectors.toList());
        }
        return PageResponse.success(articleDOPage, vos);
    }

    @Override
    public Response findArticleDetail(FindArticleDetailReqVO findArticleDetailReqVO) {
        Long ownerId = adminUserContextService.getUserId();
        Long articleId = findArticleDetailReqVO.getId();
        ArticleDO articleDO = getOwnedArticle(articleId, ownerId);

        ArticleContentDO articleContentDO = articleContentMapper.selectByArticleId(articleId);
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectByArticleId(articleId);
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByArticleId(articleId);
        List<Long> tagIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getTagId).collect(Collectors.toList());

        FindArticleDetailRspVO vo = ArticleDetailConvert.INSTANCE.convertDO2VO(articleDO);
        vo.setContent(articleContentDO == null ? null : articleContentDO.getContent());
        vo.setCategoryId(articleCategoryRelDO == null ? null : articleCategoryRelDO.getCategoryId());
        vo.setTagIds(tagIds);
        return Response.success(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateArticle(UpdateArticleReqVO updateArticleReqVO) {
        Long ownerId = adminUserContextService.getUserId();
        Long articleId = updateArticleReqVO.getId();
        getOwnedArticle(articleId, ownerId);
        CategoryDO categoryDO = getOwnedCategory(updateArticleReqVO.getCategoryId(), ownerId);

        int count = articleMapper.updateById(ArticleDO.builder()
                .id(articleId)
                .title(updateArticleReqVO.getTitle())
                .cover(updateArticleReqVO.getCover())
                .summary(updateArticleReqVO.getSummary())
                .updateTime(LocalDateTime.now())
                .build());

        if (count == 0) {
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_FOUND);
        }

        articleContentMapper.updateByArticleId(ArticleContentDO.builder()
                .articleId(articleId)
                .content(updateArticleReqVO.getContent())
                .build());

        articleCategoryRelMapper.deleteByArticleId(articleId);
        articleCategoryRelMapper.insert(ArticleCategoryRelDO.builder()
                .articleId(articleId)
                .categoryId(categoryDO.getId())
                .build());

        articleTagRelMapper.deleteByArticleId(articleId);
        insertTags(articleId, updateArticleReqVO.getTags(), ownerId);
        eventPublisher.publishEvent(new UpdateArticleEvent(this, articleId));
        return Response.success();
    }

    private ArticleDO getOwnedArticle(Long articleId, Long ownerId) {
        ArticleDO articleDO = articleMapper.selectById(articleId);
        if (articleDO == null) {
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_FOUND);
        }
        if (!Objects.equals(articleDO.getOwnerId(), ownerId)) {
            throw new BizException(ResponseCodeEnum.BLOG_ACCESS_DENIED);
        }
        return articleDO;
    }

    private CategoryDO getOwnedCategory(Long categoryId, Long ownerId) {
        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (categoryDO == null) {
            throw new BizException(ResponseCodeEnum.CATEGORY_NOT_EXISTED);
        }
        if (!Objects.equals(categoryDO.getOwnerId(), ownerId)) {
            throw new BizException(ResponseCodeEnum.BLOG_ACCESS_DENIED);
        }
        return categoryDO;
    }

    private void insertTags(Long articleId, List<String> publishTags, Long ownerId) {
        if (CollectionUtils.isEmpty(publishTags)) {
            return;
        }

        List<String> existedTags = new ArrayList<>();
        List<String> notExistTags = new ArrayList<>(publishTags);
        List<TagDO> tagDOS = tagMapper.selectByOwnerId(ownerId);

        if (!CollectionUtils.isEmpty(tagDOS)) {
            List<String> tagIds = tagDOS.stream().map(tagDO -> String.valueOf(tagDO.getId())).collect(Collectors.toList());
            existedTags = publishTags.stream().filter(tagIds::contains).collect(Collectors.toList());
            notExistTags = publishTags.stream().filter(publishTag -> !tagIds.contains(publishTag)).collect(Collectors.toList());

            Map<String, Long> tagNameIdMap = tagDOS.stream()
                    .collect(Collectors.toMap(tagDO -> tagDO.getName().toLowerCase(), TagDO::getId, (left, right) -> left));

            Iterator<String> iterator = notExistTags.iterator();
            while (iterator.hasNext()) {
                String notExistTag = iterator.next();
                Long existedId = tagNameIdMap.get(notExistTag.toLowerCase());
                if (existedId != null) {
                    iterator.remove();
                    existedTags.add(String.valueOf(existedId));
                }
            }
        }

        if (!CollectionUtils.isEmpty(existedTags)) {
            List<ArticleTagRelDO> articleTagRelDOS = Lists.newArrayList();
            existedTags.stream().distinct().forEach(tagId -> articleTagRelDOS.add(ArticleTagRelDO.builder()
                    .articleId(articleId)
                    .tagId(Long.valueOf(tagId))
                    .build()));
            articleTagRelMapper.insertBatchSomeColumn(articleTagRelDOS);
        }

        if (!CollectionUtils.isEmpty(notExistTags)) {
            List<ArticleTagRelDO> articleTagRelDOS = Lists.newArrayList();
            notExistTags.stream().map(String::trim).filter(tagName -> !tagName.isEmpty()).distinct().forEach(tagName -> {
                TagDO tagDO = TagDO.builder()
                        .name(tagName)
                        .ownerId(ownerId)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build();
                tagMapper.insert(tagDO);
                articleTagRelDOS.add(ArticleTagRelDO.builder()
                        .articleId(articleId)
                        .tagId(tagDO.getId())
                        .build());
            });
            if (!CollectionUtils.isEmpty(articleTagRelDOS)) {
                articleTagRelMapper.insertBatchSomeColumn(articleTagRelDOS);
            }
        }
    }
}
