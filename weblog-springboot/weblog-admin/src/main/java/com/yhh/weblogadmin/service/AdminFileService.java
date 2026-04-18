package com.yhh.weblogadmin.service;

import com.yhh.weblogcommon.utils.Response;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Date 2025-06-03 20:29
 * @ClassName: AdminFileService
 * @Description:
 */
public interface AdminFileService {
    /**
     * 文件上传
     * @param file
     * @return
     */
    Response uploadFile(MultipartFile file);
}
