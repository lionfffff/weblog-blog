package com.yhh.weblogcommon.aspect;

import java.lang.annotation.*;

/**
 * @Author Eason
 * @Date 2025-04-01 20:21
 * @PackageName:com.yhh.weblogcommon.aspect
 * @ClassName: ApiOperationLog
 * @Description: TODO
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiOperationLog {
    /**
     * API 功能描述
     *
     * @return
     */
    String description() default "";

}
