package com.yhh.weblogweb.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhh.weblogcommon.domain.dos.ArticleCategoryRelDO;
import com.yhh.weblogcommon.domain.dos.ArticleDO;
import com.yhh.weblogcommon.domain.dos.CategoryDO;
import com.yhh.weblogcommon.domain.mapper.ArticleCategoryRelMapper;
import com.yhh.weblogcommon.domain.mapper.ArticleMapper;
import com.yhh.weblogcommon.domain.mapper.CategoryMapper;
import com.yhh.weblogcommon.enums.ResponseCodeEnum;
import com.yhh.weblogcommon.exception.BizException;
import com.yhh.weblogcommon.utils.PageResponse;
import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.convert.ArticleConvert;
import com.yhh.weblogweb.model.vo.category.FindCategoryArticlePageListReqVO;
import com.yhh.weblogweb.model.vo.category.FindCategoryArticlePageListRspVO;
import com.yhh.weblogweb.model.vo.category.FindCategoryListReqVO;
import com.yhh.weblogweb.model.vo.category.FindCategoryListRspVO;
import com.yhh.weblogweb.service.BlogOwnerService;
import com.yhh.weblogweb.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private BlogOwnerService blogOwnerService;

    @Override
    public Response findCategoryList(FindCategoryListReqVO findCategoryListReqVO) {
        Long ownerId = blogOwnerService.getOwnerIdByUsername(findCategoryListReqVO.getBlogUsername());
        Long size = findCategoryListReqVO.getSize();

        List<CategoryDO> categoryDOS = (size == null || size == 0)
                ? categoryMapper.selectByOwnerId(ownerId)
                : categoryMapper.selectByLimit(size, ownerId);

        List<FindCategoryListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(categoryDOS)) {
            vos = categoryDOS.stream()
                    .map(categoryDO -> FindCategoryListRspVO.builder()
                            .id(categoryDO.getId())
                            .name(categoryDO.getName())
                            .articlesTotal(categoryDO.getArticlesTotal())
                            .build())
                    .collect(Collectors.toList());
        }
        return Response.success(vos);
    }

    @Override
    public Response findCategoryArticlePageList(FindCategoryArticlePageListReqVO findCategoryArticlePageListReqVO) {
        Long ownerId = blogOwnerService.getOwnerIdByUsername(findCategoryArticlePageListReqVO.getBlogUsername());
        Long current = findCategoryArticlePageListReqVO.getCurrent();
        Long size = findCategoryArticlePageListReqVO.getSize();
        Long categoryId = findCategoryArticlePageListReqVO.getId();

        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (categoryDO == null || !Objects.equals(categoryDO.getOwnerId(), ownerId)) {
            throw new BizException(ResponseCodeEnum.CATEGORY_NOT_EXISTED);
        }

        List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelMapper.selectListByCategoryId(categoryId);
        if (CollectionUtils.isEmpty(articleCategoryRelDOS)) {
            return PageResponse.success(null, null);
        }

        List<Long> articleIds = articleCategoryRelDOS.stream().map(ArticleCategoryRelDO::getArticleId).collect(Collectors.toList());
        Page<ArticleDO> page = articleMapper.selectPageListByArticleIds(current, size, articleIds, ownerId);
        List<FindCategoryArticlePageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            vos = page.getRecords().stream()
                    .map(ArticleConvert.INSTANCE::convertDO2CategoryArticleVO)
                    .collect(Collectors.toList());
        }
        return PageResponse.success(page, vos);
    }
}
