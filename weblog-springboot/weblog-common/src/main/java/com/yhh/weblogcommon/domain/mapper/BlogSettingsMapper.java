package com.yhh.weblogcommon.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yhh.weblogcommon.domain.dos.BlogSettingsDO;

/**
 * @Date 2025-06-03 20:42
 * @ClassName: BlogSettingsMapper
 * @Description:
 */
public interface BlogSettingsMapper extends BaseMapper<BlogSettingsDO> {
    default BlogSettingsDO selectByOwnerId(Long ownerId) {
        return selectOne(Wrappers.<BlogSettingsDO>lambdaQuery()
                .eq(BlogSettingsDO::getOwnerId, ownerId)
                .last("limit 1"));
    }
}
