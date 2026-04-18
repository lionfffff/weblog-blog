package com.yhh.weblogcommon.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author Eason
 * @Date 2025-04-05 0:50
 * @PackageName: com.yhh.weblogcommon.domain.dos
 * @ClassName: TagDO
 * @Description: 标签
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_tag")
public class TagDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Long ownerId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean isDeleted;

    private Integer articlesTotal;
}
