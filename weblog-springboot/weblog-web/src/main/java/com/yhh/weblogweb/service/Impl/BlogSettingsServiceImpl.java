package com.yhh.weblogweb.service.Impl;

import com.yhh.weblogcommon.domain.dos.BlogSettingsDO;
import com.yhh.weblogcommon.domain.mapper.BlogSettingsMapper;
import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.convert.BlogSettingsConvert;
import com.yhh.weblogweb.model.vo.blogsettings.FindBlogSettingsDetailReqVO;
import com.yhh.weblogweb.model.vo.blogsettings.FindBlogSettingsDetailRspVO;
import com.yhh.weblogweb.service.BlogOwnerService;
import com.yhh.weblogweb.service.BlogSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogSettingsServiceImpl implements BlogSettingsService {

    @Autowired
    private BlogSettingsMapper blogSettingsMapper;

    @Autowired
    private BlogOwnerService blogOwnerService;

    @Override
    public Response findDetail(FindBlogSettingsDetailReqVO findBlogSettingsDetailReqVO) {
        Long ownerId = blogOwnerService.getOwnerIdByUsername(findBlogSettingsDetailReqVO.getBlogUsername());
        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectByOwnerId(ownerId);
        FindBlogSettingsDetailRspVO vo = BlogSettingsConvert.INSTANCE.convertDO2VO(blogSettingsDO);
        return Response.success(vo);
    }
}
