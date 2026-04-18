package com.yhh.weblogweb.model.vo.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCategoryListReqVO {

    @NotBlank(message = "blogUsername 不能为空")
    private String blogUsername;

    private Long size;
}
