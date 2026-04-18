package com.yhh.weblogweb.service;

import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.model.vo.statistics.FindStatisticsInfoReqVO;

public interface StatisticsService {
    Response findInfo(FindStatisticsInfoReqVO findStatisticsInfoReqVO);

    Response findPVTrend(FindStatisticsInfoReqVO findStatisticsInfoReqVO);
}
