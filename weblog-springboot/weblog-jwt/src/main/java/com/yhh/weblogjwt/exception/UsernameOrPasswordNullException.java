package com.yhh.weblogjwt.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Author Eason
 * @Date 2025-04-02 11:29
 * @PackageName:com.yhh.weblogjwt.exception
 * @ClassName: UsernameOrPasswordNullException
 * @Description: TODO
 */
public class UsernameOrPasswordNullException extends AuthenticationException {
    public UsernameOrPasswordNullException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UsernameOrPasswordNullException(String msg) {
        super(msg);
    }
}

