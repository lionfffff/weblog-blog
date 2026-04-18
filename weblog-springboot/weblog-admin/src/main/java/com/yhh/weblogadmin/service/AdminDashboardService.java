package com.yhh.weblogadmin.service;

import com.yhh.weblogcommon.utils.Response;

/**
 * @Date 2025-07-16 15:45
 * @ClassName: AdminDashboardService
 * @Description: 仪表盘服务
 */
public interface AdminDashboardService {
    /**
     * 获取仪表盘基础统计信息
     * @return
     */
    Response findDashboardStatistics();
    /**
     * 获取文章发布热点统计信息
     * @return
     */
    Response findDashboardPublishArticleStatistics();

    /**
     * 获取文章最近一周 PV 访问量统计信息
     * @return
     */
    Response findDashboardPVStatistics();
}
