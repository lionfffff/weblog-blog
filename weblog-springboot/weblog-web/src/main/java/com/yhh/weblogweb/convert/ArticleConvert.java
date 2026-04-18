package com.yhh.weblogweb.convert;

import com.yhh.weblogcommon.domain.dos.ArticleDO;
import com.yhh.weblogweb.model.vo.archive.FindArchiveArticleRspVO;
import com.yhh.weblogweb.model.vo.article.FindIndexArticlePageListRspVO;
import com.yhh.weblogweb.model.vo.category.FindCategoryArticlePageListRspVO;
import com.yhh.weblogweb.model.vo.tag.FindTagArticlePageListRspVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @Date 2025-06-29 20:16
 * @ClassName: ArticleConvert
 * @Description:
 */
@Mapper
public interface ArticleConvert {
    /**
     * 初始化 convert 实例
     */
    ArticleConvert INSTANCE = Mappers.getMapper(ArticleConvert.class);

    /**
     * 将 DO 转化为 VO
     * @param bean
     * @return
     */
    @Mapping(target = "createDate", expression = "java(java.time.LocalDate.from(bean.getCreateTime()))")
    FindIndexArticlePageListRspVO convertDO2VO(ArticleDO bean);

    /**
     * 将 DO 转化为归档文章 VO
     * @param bean
     * @return
     */
    @Mapping(target = "createDate", expression = "java(java.time.LocalDate.from(bean.getCreateTime()))")
    @Mapping(target = "createMonth", expression = "java(java.time.YearMonth.from(bean.getCreateTime()))")
    FindArchiveArticleRspVO convertDO2ArchiveArticleVO(ArticleDO bean);

    /**
     * 将 DO 转换成分类文章 VO
     * @param bean
     * @return
     */
    @Mapping(target = "createDate", expression = "java(java.time.LocalDate.from(bean.getCreateTime()))")
    FindCategoryArticlePageListRspVO convertDO2CategoryArticleVO(ArticleDO bean);

    /**
     * ArticleDO -> FindTagArticlePageListRspVO
     * @param bean
     * @return
     */
    @Mapping(target = "createDate", expression = "java(java.time.LocalDate.from(bean.getCreateTime()))")
    FindTagArticlePageListRspVO convertDO2TagArticleVO(ArticleDO bean);

}
