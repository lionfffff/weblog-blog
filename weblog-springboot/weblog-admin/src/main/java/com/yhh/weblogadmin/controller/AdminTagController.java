package com.yhh.weblogadmin.controller;

import com.yhh.weblogadmin.model.vo.category.AddCategoryReqVO;
import com.yhh.weblogadmin.model.vo.category.DeleteCategoryReqVO;
import com.yhh.weblogadmin.model.vo.category.FindCategoryPageListReqVO;
import com.yhh.weblogadmin.model.vo.tag.AddTagReqVO;
import com.yhh.weblogadmin.model.vo.tag.DeleteTagReqVO;
import com.yhh.weblogadmin.model.vo.tag.FindTagPageListReqVO;
import com.yhh.weblogadmin.model.vo.tag.SearchTagReqVO;
import com.yhh.weblogadmin.service.AdminCategoryService;
import com.yhh.weblogadmin.service.AdminTagService;
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
 * @PackageName: com.yhh.weblogadmin.controller
 * @Description: 标签管理控制器
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "Admin 标签模块")
public class AdminTagController {
    @Autowired
    private AdminTagService tagService;

    @Autowired
    private AdminTagService adminTagService;
    /**
     * @author Eason
     * @date 2025-06-02 21:14
     * @description: 新增标签
    */
    @PostMapping("/tag/add")
    @ApiOperation(value = "添加标签")
    @ApiOperationLog(description = "添加标签")
    public Response addTag(@RequestBody @Validated AddTagReqVO addTagReqVO) {
        return adminTagService.addTags(addTagReqVO);
    }

    @PostMapping("/tag/list")
    @ApiOperation(value = "标签分页数据获取")
    @ApiOperationLog(description = "标签分页数据获取")
    public PageResponse findTagPageList(@RequestBody @Validated FindTagPageListReqVO  findTagPageListReqVO) {
        return adminTagService.findTagList(findTagPageListReqVO);
    }
    /**
     * @author Eason
     * @date 2025-04-28 17:19
     * @description: 删除标签接口
    */
    @PostMapping("/tag/delete")
    @ApiOperation(value = "删除标签")
    @ApiOperationLog(description = "删除标签")
    public Response deleteTag(@RequestBody @Validated DeleteTagReqVO deleteTagReqVO){
        return adminTagService.deleteTag(deleteTagReqVO);
    }

    @PostMapping("/tag/search")
    @ApiOperation(value = "标签模糊查询")
    @ApiOperationLog(description = "标签模糊查询")
    public Response searchTag(@RequestBody @Validated SearchTagReqVO searchTagReqVO){
        return adminTagService.searchTag(searchTagReqVO);
    }

    @PostMapping("/tag/select/list")
    @ApiOperation(value = "查询标签 Select 列表数据")
    @ApiOperationLog(description = "查询标签 Select 列表数据")
    public Response findTagSelectList() {
        return tagService.findTagSelectList();
    }





//
//    /**
//     * @author Eason
//     * @date 2025-04-28 20:56
//     * @description: 查询下拉列表
//    */
//    @PostMapping("/category/select/list")
//    @ApiOperation(value = "分类 Select 下拉列表数据获取")
//    @ApiOperationLog(description = "分类 Select 下拉列表数据获取")
//    public Response findCategorySelectList(){
//        return categoryService.findCategorySelectList();
//    }


    }

