package com.yhh.weblogweb.service;

import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.model.vo.article.FindArticleDetailReqVO;
import com.yhh.weblogweb.model.vo.article.FindIndexArticlePageListReqVO;

/**
 * @Date 2025-06-29 20:12
 * @ClassName: ArticleService
 * @Description:
 */
public interface ArticleService {
    /**
     * 获取首页文章分页数据
     * @param findIndexArticlePageListReqVO
     * @return
     */
    Response findArticlePageList(FindIndexArticlePageListReqVO findIndexArticlePageListReqVO);
    /**
     * 获取文章详情
     * @param findArticleDetailReqVO
     * @return
     */
    Response findArticleDetail(FindArticleDetailReqVO findArticleDetailReqVO);
}

