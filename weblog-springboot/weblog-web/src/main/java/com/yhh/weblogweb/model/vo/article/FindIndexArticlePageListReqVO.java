package com.yhh.weblogweb.model.vo.article;

import com.yhh.weblogcommon.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Date 2025-06-29 17:41
 * @ClassName: FindIndexArticlePageListReqVO
 * @Description:
 */
@Data
@Builder
@ApiModel(value = "首页查询文章分页 VO")
public class FindIndexArticlePageListReqVO extends BasePageQuery{
}

