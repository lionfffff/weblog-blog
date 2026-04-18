package com.yhh.weblogweb.controler;

import com.yhh.weblogcommon.aspect.ApiOperationLog;
import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.model.vo.search.SearchArticlePageListReqVO;
import com.yhh.weblogweb.service.SearchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date 2025-07-20 0:38
 * @ClassName: SearchController
 * @Description: 搜索控制器
 */
@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;
    @PostMapping("/article/search")
    @ApiOperation(value = "文章搜索")
    @ApiOperationLog(description = "文章搜索")
    public Response searchArticlePageList(@RequestBody @Validated SearchArticlePageListReqVO searchArticlePageListReqVO) {
        return searchService.searchArticlePageList(searchArticlePageListReqVO);
    }
}
