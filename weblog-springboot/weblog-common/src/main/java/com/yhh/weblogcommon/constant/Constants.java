package com.yhh.weblogcommon.constant;

import java.time.format.DateTimeFormatter;

/**
 * @Date 2025-07-16 16:04
 * @ClassName: Constants
 * @Description: 常量类
 */
public interface Constants {
    /**
     * 月-日 格式
     */
    DateTimeFormatter MONTH_DAY_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    /**
     * 年-月-日 小时-分钟-秒
     */
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
