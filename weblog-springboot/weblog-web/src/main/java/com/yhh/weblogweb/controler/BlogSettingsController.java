package com.yhh.weblogweb.controler;

import com.yhh.weblogcommon.aspect.ApiOperationLog;
import com.yhh.weblogcommon.utils.Response;
import com.yhh.weblogweb.model.vo.blogsettings.FindBlogSettingsDetailReqVO;
import com.yhh.weblogweb.service.BlogSettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog/settings")
@Api(tags = "博客设置")
public class BlogSettingsController {

    @Autowired
    private BlogSettingsService blogSettingsService;

    @PostMapping("/detail")
    @ApiOperation(value = "前台获取博客详情")
    @ApiOperationLog(description = "前台获取博客详情")
    public Response findDetail(@RequestBody @Validated FindBlogSettingsDetailReqVO findBlogSettingsDetailReqVO) {
        return blogSettingsService.findDetail(findBlogSettingsDetailReqVO);
    }
}
