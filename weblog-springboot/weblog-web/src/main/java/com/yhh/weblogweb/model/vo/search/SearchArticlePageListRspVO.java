package com.yhh.weblogweb.model.vo.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date 2025-07-20 0:36
 * @ClassName: SearchArticlePageListRspVO
 * @Description: 搜索文章分页列表响应参数对象
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchArticlePageListRspVO {
    /**
     * 文章 ID
     */
    private Long id;
    /**
     * 封面
     */
    private String cover;
    /**
     * 标题
     */
    private String title;
    /**
     * 摘要
     */
    private String summary;
    /**
     * 发布日期
     */
    private String createDate;
}
