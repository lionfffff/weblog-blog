package com.yhh.weblogweb.convert;

import com.yhh.weblogcommon.domain.dos.BlogSettingsDO;
import com.yhh.weblogweb.model.vo.blogsettings.FindBlogSettingsDetailRspVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Date 2025-06-30 0:47
 * @ClassName: BlogSettingsConvert
 * @Description:
 */
@Mapper
public interface BlogSettingsConvert {
    /**
     * 初始化 convert 实例
     */
    BlogSettingsConvert INSTANCE = Mappers.getMapper(BlogSettingsConvert.class);

    /**
     * 将 DO 转化为 VO
     * @param bean
     * @return
     */
    FindBlogSettingsDetailRspVO convertDO2VO(BlogSettingsDO bean);

}

