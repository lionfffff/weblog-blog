package com.yhh.weblogadmin.schedule;

import com.yhh.weblogcommon.domain.dos.StatisticsArticlePVDO;
import com.yhh.weblogcommon.domain.dos.UserDO;
import com.yhh.weblogcommon.domain.mapper.StatisticsArticlePVMapper;
import com.yhh.weblogcommon.domain.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class InitPVRecordScheduledTask {

    @Autowired
    private StatisticsArticlePVMapper articlePVMapper;

    @Autowired
    private UserMapper userMapper;

    @Scheduled(cron = "0 0 23 * * ?")
    public void execute() {
        LocalDate tomorrowDate = LocalDate.now().plusDays(1);
        List<UserDO> userDOS = userMapper.selectList(null);
        for (UserDO userDO : userDOS) {
            if (articlePVMapper.selectByDateAndOwnerId(tomorrowDate, userDO.getId()) != null) {
                continue;
            }
            articlePVMapper.insert(StatisticsArticlePVDO.builder()
                    .pvDate(tomorrowDate)
                    .ownerId(userDO.getId())
                    .pvCount(0L)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build());
        }
        log.info("init tomorrow pv records success, date: {}", tomorrowDate);
    }
}
