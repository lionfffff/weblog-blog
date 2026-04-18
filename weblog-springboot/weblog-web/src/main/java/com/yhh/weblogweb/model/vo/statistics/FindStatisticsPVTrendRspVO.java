package com.yhh.weblogweb.model.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindStatisticsPVTrendRspVO {
    private List<String> pvDates;

    private List<Long> pvCounts;

    private Long pvTotalCount;
}
