package com.yhh.weblogadmin.service;

import com.yhh.weblogadmin.model.vo.tag.AddTagReqVO;
import com.yhh.weblogadmin.model.vo.tag.DeleteTagReqVO;
import com.yhh.weblogadmin.model.vo.tag.FindTagPageListReqVO;
import com.yhh.weblogadmin.model.vo.tag.SearchTagReqVO;
import com.yhh.weblogcommon.utils.PageResponse;
import com.yhh.weblogcommon.utils.Response;

/**
 * @Author Eason
 * @Date 2025-04-05 1:02
 * @PackageName: com.yhh.weblogadmin.service
 * @ClassName: AdminCategoryService
 * @Description:
 */
public interface AdminTagService {
    Response addTags(AddTagReqVO addTagReqVO);
    /**
     * @author Eason
     * @date 2025-06-02 20:24
     * @description: 查询标签分页
    */
    PageResponse findTagList(FindTagPageListReqVO findTagPageListReqVO);
    /**
     * @author Eason
     * @date 2025-06-02 21:07
     * @description: 删除标签
    */
    Response deleteTag(DeleteTagReqVO deleteTagReqVO);
    /**
     * @author Eason
     * @date 2025-06-02 21:21
     * @description: 根据标签关键词模糊查询
    */
    Response searchTag(SearchTagReqVO searchTagReqVO);
    /**
     * 查询标签下拉列表数据
     * @return
     */
    Response findTagSelectList();
//    /**
//     * 分类分页数据查询
//     * @param findCategoryPageListReqVO
//     * @return
//     */
//    PageResponse findCategoryList(FindCategoryPageListReqVO findCategoryPageListReqVO);
//
//    /**
//     * @param deleteCategoryReqVO
//     * @return
//     */
//    Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO);
//
//    /**
//     * 查询分类下拉列表数据
//     * @return
//     */
//    Response findCategorySelectList();

}
