package com.yhh.weblogweb.model.vo.statistics;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FindStatisticsInfoReqVO {
    @NotBlank(message = "blogUsername 不能为空")
    private String blogUsername;
}
