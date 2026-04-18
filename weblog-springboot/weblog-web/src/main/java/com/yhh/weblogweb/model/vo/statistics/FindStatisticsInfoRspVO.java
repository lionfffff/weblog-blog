package com.yhh.weblogweb.model.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date 2025-07-19 23:21
 * @ClassName: FindStatisticsInfoRspVO
 * @Description: 查询统计信息响应 VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindStatisticsInfoRspVO {
    /**
     * 文章总数
     */
    private Long articleTotalCount;

    /**
     * 分类总数
     */
    private Long categoryTotalCount;

    /**
     * 标签总数
     */
    private Long tagTotalCount;

    /**
     * 总浏览量
     */
    private Long pvTotalCount;
}
