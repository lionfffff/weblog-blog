package com.yhh.weblogweb.model.vo.article;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "查询文章详情请求参数")
public class FindArticleDetailReqVO {

    @NotBlank(message = "blogUsername 不能为空")
    private String blogUsername;

    @NotNull(message = "articleId 不能为空")
    private Long articleId;
}
