package com.yhh.weblogjwt.handler;

import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogjwt.model.LoginRspVo;
import com.yhh.weblogjwt.utils.JwtTokenHelper;
import com.yhh.weblogjwt.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Eason
 * @Date 2025-04-02 11:32
 * @PackageName:com.yhh.weblogjwt.handler
 * @ClassName: RestAuthenticationSuccessHandler
 * @Description: TODO
 */
@Component
@Slf4j
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 从 authentication 对象中获取用户的 UserDetails 实例，这里是获取用户的用户名
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 通过用户名生成 Token
        String username = userDetails.getUsername();
        String token = jwtTokenHelper.generateToken(username);

        // 返回 Token
        LoginRspVo loginRspVO = LoginRspVo.builder().token(token).build();

        ResultUtil.ok(response, Response.success(loginRspVO));
    }
}
