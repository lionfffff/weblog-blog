package com.yhh.weblogweb.controler;

import com.yhh.weblogcommon.aspect.ApiOperationLog;
import com.yhh.weblogcommon.utils.JsonUtil;
import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @Author Eason
 * @Date 2025-04-01 20:33
 * @PackageName:com.yhh.weblogweb.controler
 * @ClassName: TestController
 * @Description: TODO
 */
@RestController
@Slf4j
@Api(tags = "首页模块")
public class TestController {

    @PostMapping("/test")
    @ApiOperation(value = "测试接口")
    @ApiOperationLog(description = "测试接口")
    public Response test(@RequestBody @Validated User user) {
        // 打印入参
        log.info(JsonUtil.toJsonString(user));

        // 设置三种日期字段值
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateDate(LocalDate.now());
        user.setTime(LocalTime.now());

        return Response.success(user);
    }

}
