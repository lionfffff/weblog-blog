package com.yhh.weblogadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhh.weblogadmin.model.vo.category.AddCategoryReqVO;
import com.yhh.weblogadmin.model.vo.category.DeleteCategoryReqVO;
import com.yhh.weblogadmin.model.vo.category.FindCategoryPageListReqVO;
import com.yhh.weblogadmin.model.vo.category.FindCategoryPageListRspVO;
import com.yhh.weblogadmin.service.AdminCategoryService;
import com.yhh.weblogadmin.service.AdminUserContextService;
import com.yhh.weblogcommon.domain.dos.ArticleCategoryRelDO;
import com.yhh.weblogcommon.domain.dos.CategoryDO;
import com.yhh.weblogcommon.domain.mapper.ArticleCategoryRelMapper;
import com.yhh.weblogcommon.domain.mapper.CategoryMapper;
import com.yhh.weblogcommon.enums.ResponseCodeEnum;
import com.yhh.weblogcommon.exception.BizException;
import com.yhh.weblogcommon.model.vo.SelectRspVO;
import com.yhh.weblogcommon.utils.PageResponse;
import com.yhh.weblogcommon.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;

    @Autowired
    private AdminUserContextService adminUserContextService;

    @Override
    public Response addCategory(AddCategoryReqVO addCategoryReqVO) {
        Long ownerId = adminUserContextService.getUserId();
        String categoryName = addCategoryReqVO.getName().trim();

        CategoryDO categoryDO = categoryMapper.selectByName(categoryName, ownerId);
        if (Objects.nonNull(categoryDO)) {
            throw new BizException(ResponseCodeEnum.CATEGORY_NAME_IS_EXISTED);
        }

        categoryMapper.insert(CategoryDO.builder()
                .name(categoryName)
                .ownerId(ownerId)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build());
        return Response.success();
    }

    @Override
    public PageResponse findCategoryList(FindCategoryPageListReqVO findCategoryPageListReqVO) {
        Long ownerId = adminUserContextService.getUserId();
        Page<CategoryDO> page = new Page<>(findCategoryPageListReqVO.getCurrent(), findCategoryPageListReqVO.getSize());
        String name = findCategoryPageListReqVO.getName();
        LocalDate startDate = findCategoryPageListReqVO.getStartDate();
        LocalDate endDate = findCategoryPageListReqVO.getEndDate();

        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getOwnerId, ownerId)
                .like(StringUtils.isNotBlank(name), CategoryDO::getName, name.trim())
                .ge(Objects.nonNull(startDate), CategoryDO::getCreateTime, startDate)
                .le(Objects.nonNull(endDate), CategoryDO::getCreateTime, endDate)
                .orderByDesc(CategoryDO::getCreateTime);

        Page<CategoryDO> categoryDOPage = categoryMapper.selectPage(page, wrapper);
        List<FindCategoryPageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(categoryDOPage.getRecords())) {
            vos = categoryDOPage.getRecords().stream()
                    .map(categoryDO -> FindCategoryPageListRspVO.builder()
                            .id(categoryDO.getId())
                            .name(categoryDO.getName())
                            .createTime(categoryDO.getCreateTime())
                            .articlesTotal(categoryDO.getArticlesTotal())
                            .build())
                    .collect(Collectors.toList());
        }

        return PageResponse.success(categoryDOPage, vos);
    }

    @Override
    public Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO) {
        Long ownerId = adminUserContextService.getUserId();
        Long categoryId = deleteCategoryReqVO.getId();

        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (categoryDO == null) {
            throw new BizException(ResponseCodeEnum.CATEGORY_NOT_EXISTED);
        }
        if (!Objects.equals(categoryDO.getOwnerId(), ownerId)) {
            throw new BizException(ResponseCodeEnum.BLOG_ACCESS_DENIED);
        }

        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectOneByCategoryId(categoryId);
        if (Objects.nonNull(articleCategoryRelDO)) {
            throw new BizException(ResponseCodeEnum.CATEGORY_CAN_NOT_DELETE);
        }

        categoryMapper.deleteById(categoryId);
        return Response.success();
    }

    @Override
    public Response findCategorySelectList() {
        Long ownerId = adminUserContextService.getUserId();
        List<CategoryDO> categoryDOS = categoryMapper.selectByOwnerId(ownerId);
        List<SelectRspVO> selectRspVOS = null;
        if (!CollectionUtils.isEmpty(categoryDOS)) {
            selectRspVOS = categoryDOS.stream()
                    .map(categoryDO -> SelectRspVO.builder()
                            .label(categoryDO.getName())
                            .value(categoryDO.getId())
                            .build())
                    .collect(Collectors.toList());
        }
        return Response.success(selectRspVOS);
    }
}
