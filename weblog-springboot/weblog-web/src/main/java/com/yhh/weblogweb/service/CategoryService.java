package com.yhh.weblogweb.service;

import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.model.vo.category.FindCategoryArticlePageListReqVO;
import com.yhh.weblogweb.model.vo.category.FindCategoryListReqVO;

/**
 * @Date 2025-06-29 20:21
 * @ClassName: CategoryService
 * @Description:
 */
public interface CategoryService {
    /**
     * 查询分类列表
     *
     * @return
     */
    Response findCategoryList(FindCategoryListReqVO findCategoryListReqVO);
    /**
     * 查询分类文章列表
     *
     * @param findCategoryArticlePageListReqVO
     * @return
     */
    Response findCategoryArticlePageList(FindCategoryArticlePageListReqVO findCategoryArticlePageListReqVO);
}
