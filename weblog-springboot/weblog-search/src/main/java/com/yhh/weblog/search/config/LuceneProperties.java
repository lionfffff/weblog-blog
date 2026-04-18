package com.yhh.weblog.search.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Date 2025-07-20 0:24
 * @ClassName: LuceneProperties
 * @Description: Lucene相关配置类
 */
@ConfigurationProperties(prefix = "lucene")
@Component
@Data
public class LuceneProperties {
    /**
     * 索引存放的文件夹
     */
    private String indexDir;
}
