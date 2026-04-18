package com.yhh.weblogadmin.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @Date 2025-07-14 1:10
 * @ClassName: ReadArticleEvent
 * @Description:
 */
@Getter
public class ReadArticleEvent extends ApplicationEvent {
    /**
     * 文章 ID
     */
    private Long articleId;
    public ReadArticleEvent(Object source, Long articleId) {
        super(source);
        this.articleId = articleId;
    }

}
