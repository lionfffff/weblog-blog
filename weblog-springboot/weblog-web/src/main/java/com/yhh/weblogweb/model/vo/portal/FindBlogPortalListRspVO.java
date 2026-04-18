package com.yhh.weblogweb.model.vo.portal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindBlogPortalListRspVO {
    private String username;

    private String blogName;

    private String author;

    private String avatar;

    private String introduction;

    private Long articleTotal;
}
