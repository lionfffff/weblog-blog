package com.yhh.weblogweb.service.Impl;

import com.google.common.collect.Lists;
import com.yhh.weblog.search.LuceneHelper;
import com.yhh.weblog.search.index.ArticleIndex;
import com.yhh.weblogcommon.utils.PageResponse;
import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.model.vo.search.SearchArticlePageListReqVO;
import com.yhh.weblogweb.model.vo.search.SearchArticlePageListRspVO;
import com.yhh.weblogweb.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private LuceneHelper luceneHelper;

    @Override
    public Response searchArticlePageList(SearchArticlePageListReqVO searchArticlePageListReqVO) {
        int current = Math.toIntExact(searchArticlePageListReqVO.getCurrent());
        int size = Math.toIntExact(searchArticlePageListReqVO.getSize());
        String word = searchArticlePageListReqVO.getWord();
        String blogUsername = searchArticlePageListReqVO.getBlogUsername();

        String[] columns = {ArticleIndex.COLUMN_TITLE, ArticleIndex.COLUMN_SUMMARY};
        List<Document> documents = luceneHelper.search(ArticleIndex.NAME, word, columns, 1, Integer.MAX_VALUE);
        if (CollectionUtils.isEmpty(documents)) {
            return PageResponse.success(0, current, size, Collections.emptyList());
        }

        List<Document> filteredDocuments = documents.stream()
                .filter(document -> StringUtils.equals(blogUsername, document.get(ArticleIndex.COLUMN_OWNER_USERNAME)))
                .collect(Collectors.toList());
        long total = filteredDocuments.size();
        if (CollectionUtils.isEmpty(filteredDocuments)) {
            return PageResponse.success(total, current, size, Collections.emptyList());
        }

        int start = Math.max((current - 1) * size, 0);
        int end = Math.min(start + size, filteredDocuments.size());
        if (start >= filteredDocuments.size()) {
            return PageResponse.success(total, current, size, Collections.emptyList());
        }
        List<Document> pageDocuments = filteredDocuments.subList(start, end);

        Analyzer analyzer = new SmartChineseAnalyzer();
        QueryParser parser = new QueryParser(ArticleIndex.COLUMN_TITLE, analyzer);
        Query query = null;
        try {
            query = parser.parse(word);
        } catch (ParseException ignored) {
        }

        Highlighter highlighter = new Highlighter(
                new SimpleHTMLFormatter("<span style=\"color: #f73131\">", "</span>"),
                new QueryScorer(query));

        List<SearchArticlePageListRspVO> vos = Lists.newArrayList();
        for (Document document : pageDocuments) {
            try {
                String title = document.get(ArticleIndex.COLUMN_TITLE);
                TokenStream tokenStream = analyzer.tokenStream(ArticleIndex.COLUMN_TITLE, new StringReader(title));
                String titleFragment = highlighter.getBestFragment(tokenStream, title);
                String highlightedTitle = StringUtils.isNotBlank(titleFragment) ? titleFragment : title;

                vos.add(SearchArticlePageListRspVO.builder()
                        .id(Long.valueOf(document.get(ArticleIndex.COLUMN_ID)))
                        .title(highlightedTitle)
                        .summary(document.get(ArticleIndex.COLUMN_SUMMARY))
                        .cover(document.get(ArticleIndex.COLUMN_COVER))
                        .createDate(document.get(ArticleIndex.COLUMN_CREATE_TIME))
                        .build());
            } catch (Exception ignored) {
            }
        }

        return PageResponse.success(total, current, size, vos);
    }
}
