package com.yhh.weblogweb.service;

import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.model.vo.blogsettings.FindBlogSettingsDetailReqVO;

public interface BlogSettingsService {
    Response findDetail(FindBlogSettingsDetailReqVO findBlogSettingsDetailReqVO);
}
