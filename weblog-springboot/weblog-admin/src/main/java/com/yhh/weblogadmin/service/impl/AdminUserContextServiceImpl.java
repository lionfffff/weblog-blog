package com.yhh.weblogadmin.service.impl;

import com.yhh.weblogadmin.service.AdminUserContextService;
import com.yhh.weblogcommon.domain.dos.UserDO;
import com.yhh.weblogcommon.domain.mapper.UserMapper;
import com.yhh.weblogcommon.enums.ResponseCodeEnum;
import com.yhh.weblogcommon.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AdminUserContextServiceImpl implements AdminUserContextService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !StringUtils.hasText(authentication.getName())
                || "anonymousUser".equals(authentication.getName())) {
            throw new BizException(ResponseCodeEnum.UNAUTHORIZED);
        }
        return authentication.getName();
    }

    @Override
    public Long getUserId() {
        return getUser().getId();
    }

    @Override
    public UserDO getUser() {
        UserDO userDO = userMapper.findByUsername(getUsername());
        if (userDO == null) {
            throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
        }
        return userDO;
    }
}
