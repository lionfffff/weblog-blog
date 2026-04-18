package com.yhh.weblogadmin.model.vo.article;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

/**
 * @Date 2025-06-29 13:24
 * @ClassName: DeleteArticleReqVO
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "删除文章 VO")
@Builder
public class DeleteArticleReqVO {

    @NotNull(message = "文章 ID 不能为空")
    private Long id;
}
