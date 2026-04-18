package com.yhh.weblogadmin.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @Date 2025-07-20 0:59
 * @ClassName: PublishArticleEvent
 * @Description: 文章发布事件类
 */
@Getter
public class PublishArticleEvent extends ApplicationEvent {
    /**
     * 文章 ID
     */
    private final Long articleId;

    public PublishArticleEvent(Object source, Long articleId) {
        super(source);
        this.articleId = articleId;
    }
}
