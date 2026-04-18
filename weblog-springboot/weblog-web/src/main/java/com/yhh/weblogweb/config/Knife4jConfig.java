package com.yhh.weblogweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @Author Eason
 * @Date 2025-04-01 21:17
 * @PackageName:com.yhh.weblogweb.config
 * @ClassName: Knife4jConfig
 * @Description: TODO
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {
    @Bean("webApi")
    public Docket createApiDoc() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInfo())
                // 分组名称
                .groupName("Web 前台接口")
                .select()
                // 这里指定 Controller 扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.yhh.weblogweb.controler"))
                .paths(PathSelectors.any())
                .build();
        return docket;


    }

    @Bean("adminApi")
    public Docket createAdminApiDoc() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildAdminApiInfo())
                // 分组名称
                .groupName("Admin 后台接口")
                .select()
                // 这里指定 Controller 扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.yhh.weblogadmin.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }



    /**
     * 构建 API 信息
     * @return
     */
    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title("Weblog 博客前台接口文档") // 标题
                .description("Weblog 是一款由 Spring Boot + Vue 3.2 + Vite 4.3 开发的前后端分离博客。") // 描述
                .version("1.0") // 版本号
                .build();
    }
    /**
     * 构建 Admin API 信息
     * @return
     */
    private ApiInfo buildAdminApiInfo() {
        return new ApiInfoBuilder()
                .title("Weblog 博客后台接口文档") // 标题
                .description("Weblog 是一款由 Spring Boot + Vue 3.2 + Vite 4.3 开发的前后端分离博客。") // 描述
                .version("1.0") // 版本号
                .build();
    }
}
