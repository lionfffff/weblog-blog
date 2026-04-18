package com.yhh.weblogadmin.service;

import com.yhh.weblogadmin.model.vo.blogsetting.UpdateBlogSettingsReqVO;
import com.yhh.weblogcommon.utils.Response;

/**
 * @Date 2025-06-03 20:44
 * @ClassName: AdminBlogSettingsService
 * @Description:
 */
public interface AdminBlogSettingsService {
    Response updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO);

    Response findDetail();
}
