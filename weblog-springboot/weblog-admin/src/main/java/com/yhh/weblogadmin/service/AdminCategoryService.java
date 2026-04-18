package com.yhh.weblogadmin.service;

import com.yhh.weblogadmin.model.vo.category.AddCategoryReqVO;
import com.yhh.weblogadmin.model.vo.category.DeleteCategoryReqVO;
import com.yhh.weblogadmin.model.vo.category.FindCategoryPageListReqVO;
import com.yhh.weblogcommon.utils.PageResponse;
import com.yhh.weblogcommon.utils.Response;

/**
 * @Author Eason
 * @Date 2025-04-05 1:02
 * @PackageName: com.yhh.weblogadmin.service
 * @ClassName: AdminCategoryService
 * @Description:
 */
public interface AdminCategoryService {
    Response addCategory(AddCategoryReqVO addCategoryReqVO);
    /**
     * 分类分页数据查询
     * @param findCategoryPageListReqVO
     * @return
     */
    PageResponse findCategoryList(FindCategoryPageListReqVO findCategoryPageListReqVO);

    /**
     * @param deleteCategoryReqVO
     * @return
     */
    Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO);

    /**
     * 查询分类下拉列表数据
     * @return
     */
    Response findCategorySelectList();
}
