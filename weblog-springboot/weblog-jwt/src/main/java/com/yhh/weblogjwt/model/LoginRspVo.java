package com.yhh.weblogjwt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Eason
 * @Date 2025-04-02 11:43
 * @PackageName:com.yhh.weblogjwt.model
 * @ClassName: LoginRspVo
 * @Description: TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRspVo {
   /**
    * @description: token值
    * @author Eason
    * @date 2025-04-02 11:44
    */
    private String token;
}
