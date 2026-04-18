package com.yhh.weblogadmin.model.vo.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date 2025-06-03 20:27
 * @ClassName: UploadFileRspVO
 * @Description: 文件上传Vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadFileRspVO {
    /**
     * 文件的访问链接
     */
    private String url;

}
