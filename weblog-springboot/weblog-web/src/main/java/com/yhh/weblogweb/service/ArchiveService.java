package com.yhh.weblogweb.service;

import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.model.vo.archive.FindArchiveArticlePageListReqVO;

/**
 * @Date 2025-06-30 16:43
 * @ClassName: ArchiveService
 * @Description:
 */
public interface ArchiveService {
    /**
     * 查询文章归档分页列表
     *
     * @param findArchiveArticlePageListReqVO
     * @return
     */
    Response findArchivePageList(FindArchiveArticlePageListReqVO findArchiveArticlePageListReqVO);
}
