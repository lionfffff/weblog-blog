package com.yhh.weblogadmin.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @Date 2025-07-20 12:20
 * @ClassName: UpdateArticleEvent
 * @Description: 更新文章事件
 */
@Getter
public class UpdateArticleEvent extends ApplicationEvent {

    /**
     * 文章 ID
     */
    private Long articleId;

    public UpdateArticleEvent(Object source, Long articleId) {
        super(source);
        this.articleId = articleId;
    }
}
