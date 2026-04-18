package com.yhh.weblogadmin.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
@Slf4j
public class LocalFileStorageUtil {

    @Value("${weblog.upload.local-dir:${user.dir}/uploaded-files}")
    private String localUploadDir;

    public String uploadFile(MultipartFile file) throws Exception {
        if (file == null || file.getSize() == 0) {
            throw new RuntimeException("文件大小不能为空");
        }

        String originalFileName = file.getOriginalFilename();
        String suffix = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        String objectName = UUID.randomUUID().toString().replace("-", "") + suffix;
        Path uploadDir = Paths.get(localUploadDir);
        Files.createDirectories(uploadDir);

        Path targetFile = uploadDir.resolve(objectName);
        Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(objectName)
                .toUriString();

        log.info("==> 本地上传成功，访问路径: {}", url);
        return url;
    }
}
