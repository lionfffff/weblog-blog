package com.yhh.weblogcommon.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author Eason
 * @Date 2025-04-01 21:08
 * @PackageName:com.yhh.weblogcommon.exception
 * @ClassName: BizException
 * @Description: TODO
 */
@Getter
@Setter
public class BizException extends RuntimeException {
    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;

    public BizException(BaseExceptionInterface baseExceptionInterface) {
        this.errorCode = baseExceptionInterface.getErrorCode();
        this.errorMessage = baseExceptionInterface.getErrorMessage();
    }
}
