package com.yhh.weblogadmin.model.vo.category;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;

/**
 * @Author Eason
 * @Date 2025-04-05 0:55
 * @PackageName: com.yhh.weblogadmin.model.vo.category
 * @ClassName: AddCategoryReqVO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "添加分类 VO")
public class AddCategoryReqVO {

    @NotBlank(message = "分类名称不能为空")
    @Size(min = 1, max = 10, message = "分类名称字数限制 1 ~ 10 之间")
    private String name;

}
