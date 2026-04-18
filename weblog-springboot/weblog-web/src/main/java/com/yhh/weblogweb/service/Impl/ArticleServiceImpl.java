package com.yhh.weblogweb.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yhh.weblogadmin.event.ReadArticleEvent;
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
import com.yhh.weblogweb.convert.ArticleConvert;
import com.yhh.weblogweb.markdown.MarkdownHelper;
import com.yhh.weblogweb.model.vo.article.FindArticleDetailReqVO;
import com.yhh.weblogweb.model.vo.article.FindArticleDetailRspVO;
import com.yhh.weblogweb.model.vo.article.FindIndexArticlePageListReqVO;
import com.yhh.weblogweb.model.vo.article.FindIndexArticlePageListRspVO;
import com.yhh.weblogweb.model.vo.article.FindPreNextArticleRspVO;
import com.yhh.weblogweb.model.vo.category.FindCategoryListRspVO;
import com.yhh.weblogweb.model.vo.tag.FindTagListRspVO;
import com.yhh.weblogweb.service.ArticleService;
import com.yhh.weblogweb.service.BlogOwnerService;
import com.yhh.weblogweb.utils.MarkdownStatsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleContentMapper articleContentMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private BlogOwnerService blogOwnerService;

    @Override
    public Response findArticlePageList(FindIndexArticlePageListReqVO findIndexArticlePageListReqVO) {
        Long ownerId = blogOwnerService.getOwnerIdByUsername(findIndexArticlePageListReqVO.getBlogUsername());
        Page<ArticleDO> articleDOPage = articleMapper.selectPageList(
                findIndexArticlePageListReqVO.getCurrent(),
                findIndexArticlePageListReqVO.getSize(),
                null,
                null,
                null,
                ownerId);

        List<ArticleDO> articleDOS = articleDOPage.getRecords();
        List<FindIndexArticlePageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(articleDOS)) {
            vos = articleDOS.stream()
                    .map(ArticleConvert.INSTANCE::convertDO2VO)
                    .collect(Collectors.toList());

            List<Long> articleIds = articleDOS.stream().map(ArticleDO::getId).collect(Collectors.toList());
            List<CategoryDO> categoryDOS = categoryMapper.selectByOwnerId(ownerId);
            Map<Long, String> categoryIdNameMap = categoryDOS.stream()
                    .collect(Collectors.toMap(CategoryDO::getId, CategoryDO::getName));
            List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelMapper.selectByArticleIds(articleIds);

            vos.forEach(vo -> {
                Optional<ArticleCategoryRelDO> optional = articleCategoryRelDOS.stream()
                        .filter(rel -> Objects.equals(rel.getArticleId(), vo.getId()))
                        .findAny();
                if (optional.isPresent()) {
                    ArticleCategoryRelDO relDO = optional.get();
                    vo.setCategory(FindCategoryListRspVO.builder()
                            .id(relDO.getCategoryId())
                            .name(categoryIdNameMap.get(relDO.getCategoryId()))
                            .build());
                }
            });

            List<TagDO> tagDOS = tagMapper.selectByOwnerId(ownerId);
            Map<Long, String> mapIdNameMap = tagDOS.stream()
                    .collect(Collectors.toMap(TagDO::getId, TagDO::getName));
            List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByArticleIds(articleIds);

            vos.forEach(vo -> {
                List<ArticleTagRelDO> currentTagRels = articleTagRelDOS.stream()
                        .filter(rel -> Objects.equals(rel.getArticleId(), vo.getId()))
                        .collect(Collectors.toList());
                List<FindTagListRspVO> findTagListRspVOS = Lists.newArrayList();
                currentTagRels.forEach(relDO -> findTagListRspVOS.add(FindTagListRspVO.builder()
                        .id(relDO.getTagId())
                        .name(mapIdNameMap.get(relDO.getTagId()))
                        .build()));
                vo.setTags(findTagListRspVOS);
            });
        }

        return PageResponse.success(articleDOPage, vos);
    }

    @Override
    public Response findArticleDetail(FindArticleDetailReqVO findArticleDetailReqVO) {
        Long ownerId = blogOwnerService.getOwnerIdByUsername(findArticleDetailReqVO.getBlogUsername());
        Long articleId = findArticleDetailReqVO.getArticleId();

        ArticleDO articleDO = articleMapper.selectById(articleId);
        if (articleDO == null || !Objects.equals(articleDO.getOwnerId(), ownerId)) {
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_FOUND);
        }

        ArticleContentDO articleContentDO = articleContentMapper.selectByArticleId(articleId);
        String content = articleContentDO == null ? "" : articleContentDO.getContent();
        Integer totalWords = MarkdownStatsUtil.calculateWordCount(content);

        FindArticleDetailRspVO vo = FindArticleDetailRspVO.builder()
                .title(articleDO.getTitle())
                .createTime(articleDO.getCreateTime())
                .content(MarkdownHelper.convertMarkdown2Html(content))
                .readNum(articleDO.getReadNum())
                .totalWords(totalWords)
                .readTime(MarkdownStatsUtil.calculateReadingTime(totalWords))
                .build();

        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectByArticleId(articleId);
        if (articleCategoryRelDO != null) {
            CategoryDO categoryDO = categoryMapper.selectById(articleCategoryRelDO.getCategoryId());
            if (categoryDO != null && Objects.equals(categoryDO.getOwnerId(), ownerId)) {
                vo.setCategoryId(categoryDO.getId());
                vo.setCategoryName(categoryDO.getName());
            }
        }

        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByArticleId(articleId);
        List<Long> tagIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getTagId).collect(Collectors.toList());
        List<TagDO> tagDOS = CollectionUtils.isEmpty(tagIds) ? Lists.newArrayList() : tagMapper.selectByIds(tagIds).stream()
                .filter(tagDO -> Objects.equals(tagDO.getOwnerId(), ownerId))
                .collect(Collectors.toList());
        vo.setTags(tagDOS.stream()
                .map(tagDO -> FindTagListRspVO.builder().id(tagDO.getId()).name(tagDO.getName()).build())
                .collect(Collectors.toList()));

        ArticleDO preArticleDO = articleMapper.selectPreArticle(articleId, ownerId);
        if (preArticleDO != null) {
            vo.setPreArticle(FindPreNextArticleRspVO.builder()
                    .articleId(preArticleDO.getId())
                    .articleTitle(preArticleDO.getTitle())
                    .build());
        }

        ArticleDO nextArticleDO = articleMapper.selectNextArticle(articleId, ownerId);
        if (nextArticleDO != null) {
            vo.setNextArticle(FindPreNextArticleRspVO.builder()
                    .articleId(nextArticleDO.getId())
                    .articleTitle(nextArticleDO.getTitle())
                    .build());
        }

        eventPublisher.publishEvent(new ReadArticleEvent(this, articleId));
        return Response.success(vo);
    }
}
