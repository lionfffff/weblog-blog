package com.yhh.weblogadmin.convert;

import com.yhh.weblogadmin.model.vo.article.FindArticleDetailRspVO;
import com.yhh.weblogcommon.domain.dos.ArticleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Date 2025-06-29 13:38
 * @ClassName: ArticleDetailConvert
 * @Description:
 */
@Mapper
public interface ArticleDetailConvert {
    /**
     * 初始化 convert 实例
     */
    ArticleDetailConvert INSTANCE = Mappers.getMapper(ArticleDetailConvert.class);

    /**
     * 将 DO 转化为 VO
     * @param bean
     * @return
     */
    FindArticleDetailRspVO convertDO2VO(ArticleDO bean);

}

