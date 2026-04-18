package com.yhh.weblogadmin.service;

import com.yhh.weblogadmin.model.vo.user.RegisterUserReqVO;
import com.yhh.weblogadmin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.yhh.weblogcommon.utils.Response;

public interface AdminUserService {
    Response register(RegisterUserReqVO registerUserReqVO);

    Response updatePassword(UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO);

    Response findUserInfo();
}
