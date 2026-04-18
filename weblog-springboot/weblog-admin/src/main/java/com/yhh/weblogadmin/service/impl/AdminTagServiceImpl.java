package com.yhh.weblogadmin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhh.weblogadmin.model.vo.tag.AddTagReqVO;
import com.yhh.weblogadmin.model.vo.tag.DeleteTagReqVO;
import com.yhh.weblogadmin.model.vo.tag.FindTagPageListReqVO;
import com.yhh.weblogadmin.model.vo.tag.FindTagPageListRspVO;
import com.yhh.weblogadmin.model.vo.tag.SearchTagReqVO;
import com.yhh.weblogadmin.service.AdminTagService;
import com.yhh.weblogadmin.service.AdminUserContextService;
import com.yhh.weblogcommon.domain.dos.ArticleTagRelDO;
import com.yhh.weblogcommon.domain.dos.TagDO;
import com.yhh.weblogcommon.domain.mapper.ArticleTagRelMapper;
import com.yhh.weblogcommon.domain.mapper.TagMapper;
import com.yhh.weblogcommon.enums.ResponseCodeEnum;
import com.yhh.weblogcommon.exception.BizException;
import com.yhh.weblogcommon.model.vo.SelectRspVO;
import com.yhh.weblogcommon.utils.PageResponse;
import com.yhh.weblogcommon.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminTagServiceImpl extends ServiceImpl<TagMapper, TagDO> implements AdminTagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;

    @Autowired
    private AdminUserContextService adminUserContextService;

    @Override
    public Response addTags(AddTagReqVO addTagReqVO) {
        Long ownerId = adminUserContextService.getUserId();
        List<TagDO> tagDOS = addTagReqVO.getTags().stream()
                .map(tagName -> tagName == null ? "" : tagName.trim())
                .filter(tagName -> !tagName.isEmpty())
                .distinct()
                .map(tagName -> {
                    TagDO existedTag = tagMapper.selectByKey(tagName, ownerId).stream()
                            .filter(tagDO -> tagName.equalsIgnoreCase(tagDO.getName()))
                            .findFirst()
                            .orElse(null);
                    if (existedTag != null) {
                        return null;
                    }
                    return TagDO.builder()
                            .name(tagName)
                            .ownerId(ownerId)
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(tagDOS)) {
            saveBatch(tagDOS);
        }
        return Response.success();
    }

    @Override
    public PageResponse findTagList(FindTagPageListReqVO findTagPageListReqVO) {
        Long ownerId = adminUserContextService.getUserId();
        Page<TagDO> page = tagMapper.selectPageList(findTagPageListReqVO.getCurrent(),
                findTagPageListReqVO.getSize(),
                findTagPageListReqVO.getName(),
                findTagPageListReqVO.getStartDate(),
                findTagPageListReqVO.getEndDate(),
                ownerId);

        List<FindTagPageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            vos = page.getRecords().stream()
                    .map(tagDO -> FindTagPageListRspVO.builder()
                            .id(tagDO.getId())
                            .name(tagDO.getName())
                            .createTime(tagDO.getCreateTime())
                            .articlesTotal(tagDO.getArticlesTotal())
                            .build())
                    .collect(Collectors.toList());
        }
        return PageResponse.success(page, vos);
    }

    @Override
    public Response deleteTag(DeleteTagReqVO deleteTagReqVO) {
        Long ownerId = adminUserContextService.getUserId();
        Long tagId = deleteTagReqVO.getId();

        TagDO tagDO = tagMapper.selectById(tagId);
        if (tagDO == null) {
            throw new BizException(ResponseCodeEnum.TAG_NOT_EXISTED);
        }
        if (!Objects.equals(tagDO.getOwnerId(), ownerId)) {
            throw new BizException(ResponseCodeEnum.BLOG_ACCESS_DENIED);
        }

        ArticleTagRelDO articleTagRelDO = articleTagRelMapper.selectOneByTagId(tagId);
        if (Objects.nonNull(articleTagRelDO)) {
            throw new BizException(ResponseCodeEnum.TAG_CAN_NOT_DELETE);
        }

        int count = tagMapper.deleteById(tagId);
        return count == 1 ? Response.success() : Response.fail(ResponseCodeEnum.TAG_NOT_EXISTED);
    }

    @Override
    public Response searchTag(SearchTagReqVO searchTagReqVO) {
        Long ownerId = adminUserContextService.getUserId();
        List<TagDO> tagDOS = tagMapper.selectByKey(searchTagReqVO.getKey(), ownerId);
        List<SelectRspVO> vos = null;
        if (!CollectionUtils.isEmpty(tagDOS)) {
            vos = tagDOS.stream()
                    .map(tagDO -> SelectRspVO.builder()
                            .label(tagDO.getName())
                            .value(tagDO.getId())
                            .build())
                    .collect(Collectors.toList());
        }
        return Response.success(vos);
    }

    @Override
    public Response findTagSelectList() {
        Long ownerId = adminUserContextService.getUserId();
        List<TagDO> tagDOS = tagMapper.selectByOwnerId(ownerId);
        List<SelectRspVO> vos = null;
        if (!CollectionUtils.isEmpty(tagDOS)) {
            vos = tagDOS.stream()
                    .map(tagDO -> SelectRspVO.builder()
                            .label(tagDO.getName())
                            .value(tagDO.getId())
                            .build())
                    .collect(Collectors.toList());
        }
        return Response.success(vos);
    }
}
