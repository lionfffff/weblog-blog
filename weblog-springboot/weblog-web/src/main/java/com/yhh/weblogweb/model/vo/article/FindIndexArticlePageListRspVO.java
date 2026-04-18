package com.yhh.weblogweb.model.vo.article;

import com.yhh.weblogweb.model.vo.category.FindCategoryListRspVO;
import com.yhh.weblogweb.model.vo.tag.FindTagListRspVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Date 2025-06-29 17:39
 * @ClassName: FindIndexArticlePageListRspVO
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindIndexArticlePageListRspVO {
    private Long id;
    private String cover;
    private String title;
    private LocalDate createDate;
    private String summary;
    /**
     * 文章分类
     */
    private FindCategoryListRspVO category;

    /**
     * 文章标签
     */
    private List<FindTagListRspVO> tags;
}

