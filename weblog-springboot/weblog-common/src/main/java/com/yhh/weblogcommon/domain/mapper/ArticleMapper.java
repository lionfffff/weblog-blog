package com.yhh.weblogcommon.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhh.weblogcommon.domain.dos.ArticleDO;
import com.yhh.weblogcommon.domain.dos.ArticlePublishCountDO;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public interface ArticleMapper extends BaseMapper<ArticleDO> {

    default Page<ArticleDO> selectPageList(Long current, Long size, String title, LocalDate startDate, LocalDate endDate) {
        return selectPageList(current, size, title, startDate, endDate, null);
    }

    default Page<ArticleDO> selectPageList(Long current, Long size, String title, LocalDate startDate, LocalDate endDate, Long ownerId) {
        Page<ArticleDO> page = new Page<>(current, size);
        LambdaQueryWrapper<ArticleDO> wrapper = Wrappers.<ArticleDO>lambdaQuery()
                .like(StringUtils.isNotBlank(title), ArticleDO::getTitle, title)
                .eq(Objects.nonNull(ownerId), ArticleDO::getOwnerId, ownerId)
                .ge(Objects.nonNull(startDate), ArticleDO::getCreateTime, startDate)
                .le(Objects.nonNull(endDate), ArticleDO::getCreateTime, endDate)
                .orderByDesc(ArticleDO::getCreateTime);
        return selectPage(page, wrapper);
    }

    default Page<ArticleDO> selectPageListByArticleIds(Long current, Long size, List<Long> articleIds) {
        return selectPageListByArticleIds(current, size, articleIds, null);
    }

    default Page<ArticleDO> selectPageListByArticleIds(Long current, Long size, List<Long> articleIds, Long ownerId) {
        Page<ArticleDO> page = new Page<>(current, size);
        LambdaQueryWrapper<ArticleDO> wrapper = Wrappers.<ArticleDO>lambdaQuery()
                .in(!CollectionUtils.isEmpty(articleIds), ArticleDO::getId, articleIds)
                .eq(Objects.nonNull(ownerId), ArticleDO::getOwnerId, ownerId)
                .orderByDesc(ArticleDO::getCreateTime);
        return selectPage(page, wrapper);
    }

    default ArticleDO selectPreArticle(Long articleId) {
        return selectPreArticle(articleId, null);
    }

    default ArticleDO selectPreArticle(Long articleId, Long ownerId) {
        return selectOne(Wrappers.<ArticleDO>lambdaQuery()
                .eq(Objects.nonNull(ownerId), ArticleDO::getOwnerId, ownerId)
                .orderByAsc(ArticleDO::getId)
                .gt(ArticleDO::getId, articleId)
                .last("limit 1"));
    }

    default ArticleDO selectNextArticle(Long articleId) {
        return selectNextArticle(articleId, null);
    }

    default ArticleDO selectNextArticle(Long articleId, Long ownerId) {
        return selectOne(Wrappers.<ArticleDO>lambdaQuery()
                .eq(Objects.nonNull(ownerId), ArticleDO::getOwnerId, ownerId)
                .orderByDesc(ArticleDO::getId)
                .lt(ArticleDO::getId, articleId)
                .last("limit 1"));
    }

    default int increaseReadNum(Long articleId) {
        return update(null, Wrappers.<ArticleDO>lambdaUpdate()
                .setSql("read_num = read_num + 1")
                .eq(ArticleDO::getId, articleId));
    }

    default List<ArticleDO> selectAllReadNum() {
        return selectAllReadNum(null);
    }

    default List<ArticleDO> selectAllReadNum(Long ownerId) {
        return selectList(Wrappers.<ArticleDO>lambdaQuery()
                .select(ArticleDO::getReadNum)
                .eq(Objects.nonNull(ownerId), ArticleDO::getOwnerId, ownerId));
    }

    @Select("SELECT DATE(create_time) AS date, COUNT(*) AS count " +
            "FROM t_article " +
            "WHERE create_time >= #{startDate} AND create_time < #{endDate} " +
            "GROUP BY DATE(create_time)")
    List<ArticlePublishCountDO> selectDateArticlePublishCount(LocalDate startDate, LocalDate endDate);

    @Select("SELECT DATE(create_time) AS date, COUNT(*) AS count " +
            "FROM t_article " +
            "WHERE create_time >= #{startDate} AND create_time < #{endDate} AND owner_id = #{ownerId} " +
            "GROUP BY DATE(create_time)")
    List<ArticlePublishCountDO> selectDateArticlePublishCountByOwner(LocalDate startDate, LocalDate endDate, Long ownerId);
}
