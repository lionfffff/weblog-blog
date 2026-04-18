package com.yhh.weblogadmin.model.vo.tag;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @Date 2025-06-02 17:40
 * @ClassName: AddTagReqVO
 * @Description: 标签模糊查询
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "标签模糊查询 VO")
public class SearchTagReqVO {
    @NotEmpty(message = "标签查询关键词不能为空")
    private String key;
}
