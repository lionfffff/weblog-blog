package com.yhh.weblogadmin.convert;

import com.yhh.weblogadmin.model.vo.blogsetting.FindBlogSettingsRspVO;
import com.yhh.weblogadmin.model.vo.blogsetting.UpdateBlogSettingsReqVO;
import com.yhh.weblogcommon.domain.dos.BlogSettingsDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Date 2025-06-03 20:54
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
     * 将 VO 转化为 DO
     * @param bean
     * @return
     */
    BlogSettingsDO convertVO2DO(UpdateBlogSettingsReqVO bean);
    /**
     * 将 DO 转化为 VO
     * @param bean
     * @return
     */
    FindBlogSettingsRspVO convertDO2VO(BlogSettingsDO bean);
}
