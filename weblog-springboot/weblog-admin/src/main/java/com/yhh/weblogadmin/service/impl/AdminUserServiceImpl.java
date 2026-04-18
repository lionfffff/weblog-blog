package com.yhh.weblogadmin.service.impl;

import com.yhh.weblogadmin.model.vo.user.FindUserInfoRspVO;
import com.yhh.weblogadmin.model.vo.user.RegisterUserReqVO;
import com.yhh.weblogadmin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.yhh.weblogadmin.service.AdminUserService;
import com.yhh.weblogadmin.utils.DefaultAvatarPool;
import com.yhh.weblogcommon.domain.dos.BlogSettingsDO;
import com.yhh.weblogcommon.domain.dos.UserDO;
import com.yhh.weblogcommon.domain.dos.UserRoleDo;
import com.yhh.weblogcommon.domain.mapper.BlogSettingsMapper;
import com.yhh.weblogcommon.domain.mapper.UserMapper;
import com.yhh.weblogcommon.domain.mapper.UserRoleMapper;
import com.yhh.weblogcommon.enums.ResponseCodeEnum;
import com.yhh.weblogcommon.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private BlogSettingsMapper blogSettingsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response register(RegisterUserReqVO registerUserReqVO) {
        String username = registerUserReqVO.getUsername().trim();
        if (userMapper.existsByUsername(username)) {
            return Response.fail(ResponseCodeEnum.USERNAME_ALREADY_EXISTED);
        }

        Date now = new Date();
        UserDO userDO = UserDO.builder()
                .username(username)
                .password(passwordEncoder.encode(registerUserReqVO.getPassword()))
                .createTime(now)
                .updateTime(now)
                .isDeleted(false)
                .build();
        userMapper.insert(userDO);

        userRoleMapper.insert(UserRoleDo.builder()
                .username(username)
                .role("ROLE_ADMIN")
                .createTime(now)
                .build());

        String defaultAvatar = DefaultAvatarPool.pickUnusedAvatar(blogSettingsMapper.selectList(null), username);

        blogSettingsMapper.insert(BlogSettingsDO.builder()
                .ownerId(userDO.getId())
                .name(username + " 的博客")
                .author(username)
                .introduction("欢迎来到 " + username + " 的博客")
                .avatar(defaultAvatar)
                .build());

        return Response.success();
    }

    @Override
    public Response updatePassword(UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO) {
        String username = updateAdminUserPasswordReqVO.getUsername();
        String password = updateAdminUserPasswordReqVO.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        int count = userMapper.updatePasswordByUsername(username, encodePassword);
        return count == 1 ? Response.success() : Response.fail(ResponseCodeEnum.USERNAME_NOT_FOUND);
    }

    @Override
    public Response findUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDO userDO = userMapper.findByUsername(username);
        return Response.success(FindUserInfoRspVO.builder()
                .id(userDO == null ? null : userDO.getId())
                .username(username)
                .build());
    }
}
