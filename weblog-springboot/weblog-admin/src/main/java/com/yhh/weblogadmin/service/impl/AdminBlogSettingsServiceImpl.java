package com.yhh.weblogadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhh.weblogadmin.convert.BlogSettingsConvert;
import com.yhh.weblogadmin.model.vo.blogsetting.FindBlogSettingsRspVO;
import com.yhh.weblogadmin.model.vo.blogsetting.UpdateBlogSettingsReqVO;
import com.yhh.weblogadmin.service.AdminBlogSettingsService;
import com.yhh.weblogadmin.service.AdminUserContextService;
import com.yhh.weblogcommon.domain.dos.BlogSettingsDO;
import com.yhh.weblogcommon.domain.mapper.BlogSettingsMapper;
import com.yhh.weblogcommon.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminBlogSettingsServiceImpl extends ServiceImpl<BlogSettingsMapper, BlogSettingsDO> implements AdminBlogSettingsService {

    @Autowired
    private BlogSettingsMapper blogSettingsMapper;

    @Autowired
    private AdminUserContextService adminUserContextService;

    @Override
    public Response updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO) {
        Long ownerId = adminUserContextService.getUserId();
        BlogSettingsDO existedSettings = blogSettingsMapper.selectByOwnerId(ownerId);
        BlogSettingsDO blogSettingsDO = BlogSettingsConvert.INSTANCE.convertVO2DO(updateBlogSettingsReqVO);
        blogSettingsDO.setOwnerId(ownerId);
        if (existedSettings != null) {
            blogSettingsDO.setId(existedSettings.getId());
        }
        saveOrUpdate(blogSettingsDO);
        return Response.success();
    }

    @Override
    public Response findDetail() {
        Long ownerId = adminUserContextService.getUserId();
        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectByOwnerId(ownerId);
        FindBlogSettingsRspVO vo = BlogSettingsConvert.INSTANCE.convertDO2VO(blogSettingsDO);
        return Response.success(vo);
    }
}
