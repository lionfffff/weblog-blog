package com.yhh.weblogweb.service;

import com.yhh.weblogcommon.domain.dos.UserDO;

public interface BlogOwnerService {
    Long getOwnerIdByUsername(String blogUsername);

    UserDO getOwnerByUsername(String blogUsername);
}
