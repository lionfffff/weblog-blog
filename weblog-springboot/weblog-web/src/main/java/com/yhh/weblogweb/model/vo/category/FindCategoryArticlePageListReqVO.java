package com.yhh.weblogweb.model.vo.category;

import com.yhh.weblogcommon.model.BasePageQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Date 2025-06-30 18:23
 * @ClassName: FindCategoryArticlePageListReqVO
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCategoryArticlePageListReqVO extends BasePageQuery {
    /**
     * 分类 ID
     */
    @NotNull(message = "分类 ID 不能为空")
    private Long id;
}
