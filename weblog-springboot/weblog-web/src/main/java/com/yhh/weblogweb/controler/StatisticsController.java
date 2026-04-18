package com.yhh.weblogweb.controler;

import com.yhh.weblogcommon.aspect.ApiOperationLog;
import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.model.vo.statistics.FindStatisticsInfoReqVO;
import com.yhh.weblogweb.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@Api(tags = "统计信息")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @PostMapping("/info")
    @ApiOperation(value = "前台获取统计信息")
    @ApiOperationLog(description = "前台获取统计信息")
    public Response findInfo(@RequestBody @Validated FindStatisticsInfoReqVO findStatisticsInfoReqVO) {
        return statisticsService.findInfo(findStatisticsInfoReqVO);
    }

    @PostMapping("/pv/trend")
    @ApiOperation(value = "前台获取浏览量趋势")
    @ApiOperationLog(description = "前台获取浏览量趋势")
    public Response findPVTrend(@RequestBody @Validated FindStatisticsInfoReqVO findStatisticsInfoReqVO) {
        return statisticsService.findPVTrend(findStatisticsInfoReqVO);
    }
}
