package com.yhh.weblogadmin.event.subscriber;

import com.yhh.weblogadmin.event.ReadArticleEvent;
import com.yhh.weblogcommon.domain.dos.ArticleDO;
import com.yhh.weblogcommon.domain.dos.StatisticsArticlePVDO;
import com.yhh.weblogcommon.domain.mapper.ArticleMapper;
import com.yhh.weblogcommon.domain.mapper.StatisticsArticlePVMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
public class ReadArticleSubscriber implements ApplicationListener<ReadArticleEvent> {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private StatisticsArticlePVMapper articlePVMapper;

    @Override
    @Async("threadPoolTaskExecutor")
    public void onApplicationEvent(ReadArticleEvent event) {
        Long articleId = event.getArticleId();
        ArticleDO articleDO = articleMapper.selectById(articleId);
        if (articleDO == null) {
            return;
        }

        articleMapper.increaseReadNum(articleId);

        LocalDate currDate = LocalDate.now();
        Long ownerId = articleDO.getOwnerId();
        int updated = articlePVMapper.increasePVCount(currDate, ownerId);
        if (updated == 0) {
            articlePVMapper.insert(StatisticsArticlePVDO.builder()
                    .pvDate(currDate)
                    .ownerId(ownerId)
                    .pvCount(1L)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build());
        }

        log.info("read article success, articleId: {}, ownerId: {}", articleId, ownerId);
    }
}
