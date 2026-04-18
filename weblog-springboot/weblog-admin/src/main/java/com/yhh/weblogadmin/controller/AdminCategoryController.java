package com.yhh.weblogadmin.controller;

import com.yhh.weblogadmin.model.vo.category.AddCategoryReqVO;
import com.yhh.weblogadmin.model.vo.category.DeleteCategoryReqVO;
import com.yhh.weblogadmin.model.vo.category.FindCategoryPageListReqVO;
import com.yhh.weblogadmin.service.AdminCategoryService;
import com.yhh.weblogcommon.aspect.ApiOperationLog;
import com.yhh.weblogcommon.utils.PageResponse;
import com.yhh.weblogcommon.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date 2025-04-05 1:01
 * @PackageName:com.yhh.weblogadmin.controller
 * @ClassName: AdminCategoryController
 * @Description: 分类管理控制器
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "Admin 分类模块")
public class AdminCategoryController {

    @Autowired
    private AdminCategoryService categoryService;

    @PostMapping("/category/add")
    @ApiOperation(value = "添加分类")
    @ApiOperationLog(description = "添加分类")
    public Response addCategory(@RequestBody @Validated AddCategoryReqVO addCategoryReqVO) {
        return categoryService.addCategory(addCategoryReqVO);
    }

    @PostMapping("/category/list")
    @ApiOperation(value = "分类分页数据获取")
    @ApiOperationLog(description = "分类分页数据获取")
    public PageResponse findCategoryList(@RequestBody @Validated FindCategoryPageListReqVO findCategoryPageListReqVO) {
        return categoryService.findCategoryList(findCategoryPageListReqVO);
    }

    /**
     * @author Eason
     * @date 2025-04-28 17:19
     * @description: 删除分类接口
    */
    @PostMapping("/category/delete")
    @ApiOperation(value = "删除分类")
    @ApiOperationLog(description = "删除分类")
    public Response deleteCategory(@RequestBody @Validated DeleteCategoryReqVO deleteCategoryReqVO){
        return categoryService.deleteCategory(deleteCategoryReqVO);
    }

    /**
     * @author Eason
     * @date 2025-04-28 20:56
     * @description: 查询下拉列表
    */
    @PostMapping("/category/select/list")
    @ApiOperation(value = "分类 Select 下拉列表数据获取")
    @ApiOperationLog(description = "分类 Select 下拉列表数据获取")
    public Response findCategorySelectList(){
        return categoryService.findCategorySelectList();
    }



}

