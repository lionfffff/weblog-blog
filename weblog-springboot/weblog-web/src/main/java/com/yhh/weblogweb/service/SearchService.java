package com.yhh.weblogweb.service;

import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.model.vo.search.SearchArticlePageListReqVO;

/**
 * @Date 2025-07-20 0:39
 * @ClassName: SearchService
 * @Description: 搜索服务接口
 */
public interface SearchService {
    /**
     * 搜索文章分页列表
     *
     * @param searchArticlePageListReqVO 搜索文章分页列表请求参数对象
     * @return Response 返回搜索结果
     */
    Response searchArticlePageList(SearchArticlePageListReqVO searchArticlePageListReqVO);
}
