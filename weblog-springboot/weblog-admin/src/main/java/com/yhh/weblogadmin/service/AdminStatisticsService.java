package com.yhh.weblogadmin.service;

/**
 * @author: Eason
 * @Date: 2025/7/24 01:10
 * @description: 统计服务
 **/

public interface AdminStatisticsService {
    /**
     * 统计各分类下文章总数
     */
    void statisticsCategoryArticleTotal();
    /**
     * 统计各标签下文章总数
     */
    void statisticsTagArticleTotal();

}
