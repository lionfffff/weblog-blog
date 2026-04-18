package com.yhh.weblogweb.controler;

import com.yhh.weblogcommon.aspect.ApiOperationLog;
import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.service.BlogPortalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog/portal")
@Api(tags = "博客门户")
public class BlogPortalController {

    @Autowired
    private BlogPortalService blogPortalService;

    @PostMapping("/list")
    @ApiOperation(value = "查询博客选择页列表")
    @ApiOperationLog(description = "查询博客选择页列表")
    public Response findBlogPortalList() {
        return blogPortalService.findBlogPortalList();
    }
}
