package com.yhh.weblogadmin.service.impl;

import com.yhh.weblogadmin.model.vo.file.UploadFileRspVO;
import com.yhh.weblogadmin.service.AdminFileService;
import com.yhh.weblogadmin.utils.LocalFileStorageUtil;
import com.yhh.weblogadmin.utils.MinioUtil;
import com.yhh.weblogcommon.enums.ResponseCodeEnum;
import com.yhh.weblogcommon.exception.BizException;
import com.yhh.weblogcommon.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Date 2025-06-03 20:29
 * @ClassName: AdminFileServiceImpl
 * @Description:
 */
@Service
@Slf4j
public class AdminFileServiceImpl implements AdminFileService {
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private LocalFileStorageUtil localFileStorageUtil;

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @Override
    public Response uploadFile(MultipartFile file) {
        try {
            // 上传文件
            String url = minioUtil.uploadFile(file);

            // 构建成功返参，将图片的访问链接返回
            return Response.success(UploadFileRspVO.builder().url(url).build());
        } catch (Exception e) {
            log.warn("==> 上传文件至 Minio 失败，开始回退到本地存储: ", e);
            try {
                String url = localFileStorageUtil.uploadFile(file);
                return Response.success(UploadFileRspVO.builder().url(url).build());
            } catch (Exception localException) {
                log.error("==> 本地上传文件也失败: ", localException);
                // 手动抛出业务异常，提示 “文件上传失败”
                throw new BizException(ResponseCodeEnum.FILE_UPLOAD_FAILED);
            }
        }
    }
}
