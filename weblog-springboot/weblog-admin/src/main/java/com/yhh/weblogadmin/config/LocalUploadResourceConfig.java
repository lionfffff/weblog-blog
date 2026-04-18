package com.yhh.weblogadmin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Paths;

/**
 * Expose locally uploaded files during development so the frontend can preview them
 * without depending on MinIO.
 */
@Configuration
public class LocalUploadResourceConfig implements WebMvcConfigurer {

    @Value("${weblog.upload.local-dir:${user.dir}/uploaded-files}")
    private String localUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = Paths.get(localUploadDir)
                .toAbsolutePath()
                .toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + File.separator);
    }
}
