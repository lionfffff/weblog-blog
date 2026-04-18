package com.yhh.weblogadmin.service;

import com.yhh.weblogadmin.model.vo.article.*;
import com.yhh.weblogcommon.utils.Response;

/**
 * @Date 2025-06-03 22:22
 * @ClassName: AdminArticleService
 * @Description:
 */
public interface AdminArticleService {
    /**
     * 发布文章
     * @param publishArticleReqVO
     * @return
     */
    Response publishArticle(PublishArticleReqVO publishArticleReqVO);

    /**
     * 删除文章
     * @param deleteArticleReqVO
     * @return
     */
    Response deleteArticle(DeleteArticleReqVO deleteArticleReqVO);
    /**
     * 查询文章分页数据
     * @param findArticlePageListReqVO
     * @return
     */
    Response findArticlePageList(FindArticlePageListReqVO findArticlePageListReqVO);
    /**
     * 查询文章详情
     * @param findArticlePageListReqVO
     * @return
     */
    Response findArticleDetail(FindArticleDetailReqVO findArticlePageListReqVO);
    /**
     * 更新文章
     * @param updateArticleReqVO
     * @return
     */
    Response updateArticle(UpdateArticleReqVO updateArticleReqVO);
}
