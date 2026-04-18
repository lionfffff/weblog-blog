package com.yhh.weblogweb.model.vo.tag;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FindTagListReqVO {
    @NotBlank(message = "blogUsername 不能为空")
    private String blogUsername;
}
