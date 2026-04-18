package com.yhh.weblogweb.model.vo.archive;

import com.yhh.weblogcommon.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * @Date 2025-06-30 16:41
 * @ClassName: FindArchiveArticlePageListReqVO
 * @Description:
 */
@Data
@Builder
@ApiModel(value = "文章归档分页 VO")
public class FindArchiveArticlePageListReqVO extends BasePageQuery {
}
