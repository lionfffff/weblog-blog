package com.yhh.weblog.search.runner;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.yhh.weblog.search.LuceneHelper;
import com.yhh.weblog.search.config.LuceneProperties;
import com.yhh.weblog.search.index.ArticleIndex;
import com.yhh.weblogcommon.constant.Constants;
import com.yhh.weblogcommon.domain.dos.ArticleContentDO;
import com.yhh.weblogcommon.domain.dos.ArticleDO;
import com.yhh.weblogcommon.domain.dos.UserDO;
import com.yhh.weblogcommon.domain.mapper.ArticleContentMapper;
import com.yhh.weblogcommon.domain.mapper.ArticleMapper;
import com.yhh.weblogcommon.domain.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InitLuceneIndexRunner implements CommandLineRunner {

    @Autowired
    private LuceneProperties luceneProperties;

    @Autowired
    private LuceneHelper luceneHelper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleContentMapper articleContentMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void run(String... args) {
        List<ArticleDO> articleDOS = articleMapper.selectList(Wrappers.emptyWrapper());
        if (articleDOS.isEmpty() || StringUtils.isBlank(luceneProperties.getIndexDir())) {
            return;
        }

        Map<Long, String> ownerUsernameMap = userMapper.selectList(null).stream()
                .collect(Collectors.toMap(UserDO::getId, UserDO::getUsername));

        List<Document> documents = Lists.newArrayList();
        for (ArticleDO articleDO : articleDOS) {
            ArticleContentDO articleContentDO = articleContentMapper.selectByArticleId(articleDO.getId());
            Document document = new Document();
            document.add(new TextField(ArticleIndex.COLUMN_ID, String.valueOf(articleDO.getId()), Field.Store.YES));
            document.add(new TextField(ArticleIndex.COLUMN_TITLE, articleDO.getTitle(), Field.Store.YES));
            document.add(new TextField(ArticleIndex.COLUMN_COVER, articleDO.getCover(), Field.Store.YES));
            document.add(new TextField(ArticleIndex.COLUMN_SUMMARY, articleDO.getSummary(), Field.Store.YES));
            document.add(new TextField(ArticleIndex.COLUMN_CONTENT, articleContentDO == null ? "" : articleContentDO.getContent(), Field.Store.YES));
            document.add(new TextField(ArticleIndex.COLUMN_CREATE_TIME, Constants.DATE_TIME_FORMATTER.format(articleDO.getCreateTime()), Field.Store.YES));
            document.add(new TextField(ArticleIndex.COLUMN_OWNER_ID, String.valueOf(articleDO.getOwnerId()), Field.Store.YES));
            document.add(new TextField(ArticleIndex.COLUMN_OWNER_USERNAME, ownerUsernameMap.getOrDefault(articleDO.getOwnerId(), ""), Field.Store.YES));
            documents.add(document);
        }

        luceneHelper.createIndex(ArticleIndex.NAME, documents);
    }
}
