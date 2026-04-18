package com.yhh.weblogcommon.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yhh.weblogcommon.domain.dos.CategoryDO;

import java.util.List;

public interface CategoryMapper extends BaseMapper<CategoryDO> {

    default CategoryDO selectByName(String categoryName) {
        return selectByName(categoryName, null);
    }

    default CategoryDO selectByName(String categoryName, Long ownerId) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getName, categoryName)
                .eq(ownerId != null, CategoryDO::getOwnerId, ownerId);
        return selectOne(wrapper);
    }

    default List<CategoryDO> selectByLimit(Long limit) {
        return selectByLimit(limit, null);
    }

    default List<CategoryDO> selectByLimit(Long limit, Long ownerId) {
        return selectList(Wrappers.<CategoryDO>lambdaQuery()
                .eq(ownerId != null, CategoryDO::getOwnerId, ownerId)
                .orderByDesc(CategoryDO::getArticlesTotal)
                .last(String.format("LIMIT %d", limit)));
    }

    default List<CategoryDO> selectByOwnerId(Long ownerId) {
        return selectList(Wrappers.<CategoryDO>lambdaQuery()
                .eq(CategoryDO::getOwnerId, ownerId)
                .orderByDesc(CategoryDO::getArticlesTotal)
                .orderByDesc(CategoryDO::getCreateTime));
    }
}
