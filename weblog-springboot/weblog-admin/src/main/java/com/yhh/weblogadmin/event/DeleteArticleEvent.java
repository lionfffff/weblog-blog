package com.yhh.weblogadmin.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @Date 2025-07-20 12:16
 * @ClassName: DeleteArticleEvent
 * @Description: 删除文章事件
 */
@Getter
public class DeleteArticleEvent extends ApplicationEvent {
    /**
     * 文章 ID
     */
    private Long articleId;

    public DeleteArticleEvent(Object source, Long articleId) {
        super(source);
        this.articleId = articleId;
    }
}
