package com.yhh.weblogweb.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yhh.weblogcommon.domain.dos.ArticleDO;
import com.yhh.weblogcommon.domain.dos.BlogSettingsDO;
import com.yhh.weblogcommon.domain.dos.CategoryDO;
import com.yhh.weblogcommon.domain.dos.TagDO;
import com.yhh.weblogcommon.domain.dos.UserDO;
import com.yhh.weblogcommon.domain.dos.StatisticsArticlePVDO;
import com.yhh.weblogcommon.domain.mapper.ArticleMapper;
import com.yhh.weblogcommon.domain.mapper.BlogSettingsMapper;
import com.yhh.weblogcommon.domain.mapper.CategoryMapper;
import com.yhh.weblogcommon.domain.mapper.TagMapper;
import com.yhh.weblogcommon.domain.mapper.UserMapper;
import com.yhh.weblogcommon.domain.mapper.StatisticsArticlePVMapper;
import com.yhh.weblogcommon.enums.ResponseCodeEnum;
import com.yhh.weblogcommon.exception.BizException;
import com.yhh.weblogweb.service.BlogOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class BlogOwnerServiceImpl implements BlogOwnerService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlogSettingsMapper blogSettingsMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private StatisticsArticlePVMapper statisticsArticlePVMapper;

    @Override
    public Long getOwnerIdByUsername(String blogUsername) {
        return getOwnerByUsername(blogUsername).getId();
    }

    @Override
    public UserDO getOwnerByUsername(String blogUsername) {
        if (!StringUtils.hasText(blogUsername)) {
            throw new BizException(ResponseCodeEnum.BLOG_NOT_FOUND);
        }
        UserDO userDO = userMapper.findByUsername(blogUsername.trim());
        if (userDO == null) {
            throw new BizException(ResponseCodeEnum.BLOG_NOT_FOUND);
        }
        ensureLegacyAdminData(userDO);
        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectByOwnerId(userDO.getId());
        if (blogSettingsDO == null) {
            throw new BizException(ResponseCodeEnum.BLOG_NOT_FOUND);
        }
        return userDO;
    }

    private void ensureLegacyAdminData(UserDO userDO) {
        if (userDO == null || !"admin".equals(userDO.getUsername())) {
            return;
        }

        if (blogSettingsMapper.selectByOwnerId(userDO.getId()) != null) {
            return;
        }

        blogSettingsMapper.update(null, Wrappers.<BlogSettingsDO>lambdaUpdate()
                .set(BlogSettingsDO::getOwnerId, userDO.getId())
                .isNull(BlogSettingsDO::getOwnerId));

        articleMapper.update(null, Wrappers.<ArticleDO>lambdaUpdate()
                .set(ArticleDO::getOwnerId, userDO.getId())
                .isNull(ArticleDO::getOwnerId));

        categoryMapper.update(null, Wrappers.<CategoryDO>lambdaUpdate()
                .set(CategoryDO::getOwnerId, userDO.getId())
                .isNull(CategoryDO::getOwnerId));

        tagMapper.update(null, Wrappers.<TagDO>lambdaUpdate()
                .set(TagDO::getOwnerId, userDO.getId())
                .isNull(TagDO::getOwnerId));

        statisticsArticlePVMapper.update(null, Wrappers.<StatisticsArticlePVDO>lambdaUpdate()
                .set(StatisticsArticlePVDO::getOwnerId, userDO.getId())
                .isNull(StatisticsArticlePVDO::getOwnerId));
    }
}
