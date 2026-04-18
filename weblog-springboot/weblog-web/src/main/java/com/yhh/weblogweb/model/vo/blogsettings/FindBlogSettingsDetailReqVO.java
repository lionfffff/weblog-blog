package com.yhh.weblogweb.model.vo.blogsettings;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "查询博客设置请求参数")
public class FindBlogSettingsDetailReqVO {

    @NotBlank(message = "blogUsername 不能为空")
    private String blogUsername;
}
