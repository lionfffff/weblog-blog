package com.yhh.weblogweb.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yhh.weblogcommon.domain.dos.ArticleDO;
import com.yhh.weblogcommon.domain.dos.BlogSettingsDO;
import com.yhh.weblogcommon.domain.dos.CategoryDO;
import com.yhh.weblogcommon.domain.dos.TagDO;
import com.yhh.weblogcommon.domain.dos.StatisticsArticlePVDO;
import com.yhh.weblogcommon.domain.dos.UserDO;
import com.yhh.weblogcommon.domain.mapper.CategoryMapper;
import com.yhh.weblogcommon.domain.mapper.ArticleMapper;
import com.yhh.weblogcommon.domain.mapper.BlogSettingsMapper;
import com.yhh.weblogcommon.domain.mapper.TagMapper;
import com.yhh.weblogcommon.domain.mapper.UserMapper;
import com.yhh.weblogcommon.domain.mapper.StatisticsArticlePVMapper;
import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.model.vo.portal.FindBlogPortalListRspVO;
import com.yhh.weblogweb.service.BlogPortalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BlogPortalServiceImpl implements BlogPortalService {

    @Autowired
    private BlogSettingsMapper blogSettingsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private StatisticsArticlePVMapper statisticsArticlePVMapper;

    @Override
    public Response findBlogPortalList() {
        migrateLegacyAdminDataIfNeeded();
        List<BlogSettingsDO> settingsDOS = blogSettingsMapper.selectList(Wrappers.<BlogSettingsDO>lambdaQuery()
                .isNotNull(BlogSettingsDO::getOwnerId));
        if (CollectionUtils.isEmpty(settingsDOS)) {
            return Response.success(Collections.emptyList());
        }

        List<Long> ownerIds = settingsDOS.stream().map(BlogSettingsDO::getOwnerId).collect(Collectors.toList());
        Map<Long, UserDO> ownerMap = userMapper.selectList(Wrappers.<UserDO>lambdaQuery().in(UserDO::getId, ownerIds))
                .stream()
                .collect(Collectors.toMap(UserDO::getId, userDO -> userDO));

        List<FindBlogPortalListRspVO> vos = settingsDOS.stream()
                .map(settingsDO -> {
                    UserDO userDO = ownerMap.get(settingsDO.getOwnerId());
                    if (userDO == null) {
                        return null;
                    }
                    Long articleTotal = articleMapper.selectCount(Wrappers.<ArticleDO>lambdaQuery()
                            .eq(ArticleDO::getOwnerId, settingsDO.getOwnerId()));
                    return FindBlogPortalListRspVO.builder()
                            .username(userDO.getUsername())
                            .blogName(settingsDO.getName())
                            .author(settingsDO.getAuthor())
                            .avatar(settingsDO.getAvatar())
                            .introduction(settingsDO.getIntroduction())
                            .articleTotal(articleTotal)
                            .build();
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());

        return Response.success(vos);
    }

    private void migrateLegacyAdminDataIfNeeded() {
        UserDO adminUser = userMapper.findByUsername("admin");
        if (adminUser == null) {
            return;
        }

        if (blogSettingsMapper.selectByOwnerId(adminUser.getId()) != null) {
            return;
        }

        blogSettingsMapper.update(null, Wrappers.<BlogSettingsDO>lambdaUpdate()
                .set(BlogSettingsDO::getOwnerId, adminUser.getId())
                .isNull(BlogSettingsDO::getOwnerId));

        articleMapper.update(null, Wrappers.<ArticleDO>lambdaUpdate()
                .set(ArticleDO::getOwnerId, adminUser.getId())
                .isNull(ArticleDO::getOwnerId));

        categoryMapper.update(null, Wrappers.<CategoryDO>lambdaUpdate()
                .set(CategoryDO::getOwnerId, adminUser.getId())
                .isNull(CategoryDO::getOwnerId));

        tagMapper.update(null, Wrappers.<TagDO>lambdaUpdate()
                .set(TagDO::getOwnerId, adminUser.getId())
                .isNull(TagDO::getOwnerId));

        statisticsArticlePVMapper.update(null, Wrappers.<StatisticsArticlePVDO>lambdaUpdate()
                .set(StatisticsArticlePVDO::getOwnerId, adminUser.getId())
                .isNull(StatisticsArticlePVDO::getOwnerId));
    }
}
