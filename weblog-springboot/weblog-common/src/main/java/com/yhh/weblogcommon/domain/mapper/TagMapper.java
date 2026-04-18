package com.yhh.weblogcommon.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhh.weblogcommon.domain.dos.TagDO;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public interface TagMapper extends BaseMapper<TagDO> {

    default Page<TagDO> selectPageList(long current, long size, String name, LocalDate startData, LocalDate endDate) {
        return selectPageList(current, size, name, startData, endDate, null);
    }

    default Page<TagDO> selectPageList(long current, long size, String name, LocalDate startData, LocalDate endDate, Long ownerId) {
        Page<TagDO> page = new Page<>(current, size);
        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Objects.nonNull(name), TagDO::getName, name)
                .eq(Objects.nonNull(ownerId), TagDO::getOwnerId, ownerId)
                .ge(Objects.nonNull(startData), TagDO::getCreateTime, startData)
                .le(Objects.nonNull(endDate), TagDO::getCreateTime, endDate)
                .orderByDesc(TagDO::getCreateTime);
        return selectPage(page, wrapper);
    }

    default List<TagDO> selectByKey(String key) {
        return selectByKey(key, null);
    }

    default List<TagDO> selectByKey(String key, Long ownerId) {
        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(TagDO::getName, key)
                .eq(Objects.nonNull(ownerId), TagDO::getOwnerId, ownerId)
                .orderByDesc(TagDO::getCreateTime);
        return selectList(wrapper);
    }

    default List<TagDO> selectByIds(List<Long> tagIds) {
        return selectList(Wrappers.<TagDO>lambdaQuery()
                .in(TagDO::getId, tagIds));
    }

    default List<TagDO> selectByOwnerId(Long ownerId) {
        return selectList(Wrappers.<TagDO>lambdaQuery()
                .eq(TagDO::getOwnerId, ownerId)
                .orderByDesc(TagDO::getArticlesTotal)
                .orderByDesc(TagDO::getCreateTime));
    }
}
