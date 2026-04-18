package com.yhh.weblogadmin.model.vo.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author Eason
 * @Date 2025-04-18 13:42
 * @PackageName: com.yhh.weblogadmin.model.vo.category
 * @ClassName: FindCategoryPageListRspVO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCategoryPageListRspVO {
    /**
     * 分类 ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 文章总数
     */
    private Integer articlesTotal;

}
