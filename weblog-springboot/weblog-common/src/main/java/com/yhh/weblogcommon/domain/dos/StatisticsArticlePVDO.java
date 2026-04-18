package com.yhh.weblogcommon.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Date 2025-07-16 15:55
 * @ClassName: StatisticsArticlePVDO
 * @Description: 统计文章PV数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_statistics_article_pv")
public class StatisticsArticlePVDO {
    @TableId(type = IdType.AUTO)
    private Long id;

    private LocalDate pvDate;

    private Long ownerId;

    private Long pvCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
