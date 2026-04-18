package com.yhh.weblogcommon.model;

import lombok.Data;

/**
 * @Author Eason
 * @Date 2025-04-18 13:28
 * @PackageName:com.yhh.weblogcommon.model
 * @ClassName: BasePageQuery
 * @Description: TODO
 */
@Data
public class BasePageQuery {
    private String blogUsername;

    /**
     * 当前页码, 默认第一页
     */
    private Long current = 1L;
    /**
     * 每页展示的数据数量，默认每页展示 10 条数据
     */
    private Long size = 10L;
}
