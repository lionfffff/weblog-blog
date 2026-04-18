package com.yhh.weblogadmin.event.subscriber;

import com.yhh.weblog.search.LuceneHelper;
import com.yhh.weblog.search.index.ArticleIndex;
import com.yhh.weblogadmin.event.PublishArticleEvent;
import com.yhh.weblogcommon.constant.Constants;
import com.yhh.weblogcommon.domain.dos.ArticleContentDO;
import com.yhh.weblogcommon.domain.dos.ArticleDO;
import com.yhh.weblogcommon.domain.dos.UserDO;
import com.yhh.weblogcommon.domain.mapper.ArticleContentMapper;
import com.yhh.weblogcommon.domain.mapper.ArticleMapper;
import com.yhh.weblogcommon.domain.mapper.UserMapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PublishArticleSubscriber implements ApplicationListener<PublishArticleEvent> {

    @Autowired
    private LuceneHelper luceneHelper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleContentMapper articleContentMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Async("threadPoolTaskExecutor")
    public void onApplicationEvent(PublishArticleEvent event) {
        Long articleId = event.getArticleId();
        ArticleDO articleDO = articleMapper.selectById(articleId);
        ArticleContentDO articleContentDO = articleContentMapper.selectByArticleId(articleId);
        UserDO owner = userMapper.selectById(articleDO.getOwnerId());

        Document document = new Document();
        document.add(new TextField(ArticleIndex.COLUMN_ID, String.valueOf(articleId), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_TITLE, articleDO.getTitle(), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_COVER, articleDO.getCover(), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_SUMMARY, articleDO.getSummary(), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_CONTENT, articleContentDO == null ? "" : articleContentDO.getContent(), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_CREATE_TIME, Constants.DATE_TIME_FORMATTER.format(articleDO.getCreateTime()), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_OWNER_ID, String.valueOf(articleDO.getOwnerId()), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_OWNER_USERNAME, owner == null ? "" : owner.getUsername(), Field.Store.YES));

        luceneHelper.addDocument(ArticleIndex.NAME, document);
    }
}
