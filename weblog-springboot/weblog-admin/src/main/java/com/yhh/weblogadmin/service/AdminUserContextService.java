package com.yhh.weblogadmin.service;

import com.yhh.weblogcommon.domain.dos.UserDO;

public interface AdminUserContextService {
    String getUsername();

    Long getUserId();

    UserDO getUser();
}
