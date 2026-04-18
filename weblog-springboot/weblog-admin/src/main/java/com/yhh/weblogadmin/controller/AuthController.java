package com.yhh.weblogadmin.controller;

import com.yhh.weblogadmin.model.vo.user.RegisterUserReqVO;
import com.yhh.weblogadmin.service.AdminUserService;
import com.yhh.weblogcommon.aspect.ApiOperationLog;
import com.yhh.weblogcommon.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "认证")
public class AuthController {

    @Autowired
    private AdminUserService adminUserService;

    @PostMapping("/register")
    @ApiOperation(value = "注册管理员账号")
    @ApiOperationLog(description = "注册管理员账号")
    public Response register(@RequestBody @Validated RegisterUserReqVO registerUserReqVO) {
        return adminUserService.register(registerUserReqVO);
    }
}
