package com.yhh.weblogcommon.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author Eason
 * @Date 2025-04-02 17:07
 * @PackageName:com.yhh.weblogcommon.domain.dos
 * @ClassName: UserRoleDo
 * @Description: TODO
 */
@Data
@Builder
@TableName("t_user_role")
public class UserRoleDo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String role;
    private Date createTime;
}
