package com.yhh.weblogcommon.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yhh.weblogcommon.domain.dos.StatisticsArticlePVDO;

import java.time.LocalDate;
import java.util.List;

public interface StatisticsArticlePVMapper extends BaseMapper<StatisticsArticlePVDO> {

    default int increasePVCount(LocalDate date) {
        return increasePVCount(date, null);
    }

    default int increasePVCount(LocalDate date, Long ownerId) {
        return update(null, Wrappers.<StatisticsArticlePVDO>lambdaUpdate()
                .setSql("pv_count = pv_count + 1")
                .eq(StatisticsArticlePVDO::getPvDate, date)
                .eq(ownerId != null, StatisticsArticlePVDO::getOwnerId, ownerId));
    }

    default List<StatisticsArticlePVDO> selectLatestWeekRecords() {
        return selectLatestWeekRecords(null);
    }

    default List<StatisticsArticlePVDO> selectLatestWeekRecords(Long ownerId) {
        return selectList(Wrappers.<StatisticsArticlePVDO>lambdaQuery()
                .eq(ownerId != null, StatisticsArticlePVDO::getOwnerId, ownerId)
                .le(StatisticsArticlePVDO::getPvDate, LocalDate.now().plusDays(1))
                .orderByDesc(StatisticsArticlePVDO::getPvDate)
                .last("limit 7"));
    }

    default StatisticsArticlePVDO selectByDateAndOwnerId(LocalDate date, Long ownerId) {
        return selectOne(Wrappers.<StatisticsArticlePVDO>lambdaQuery()
                .eq(StatisticsArticlePVDO::getPvDate, date)
                .eq(StatisticsArticlePVDO::getOwnerId, ownerId)
                .last("limit 1"));
    }
}
