package cn.marsLottery.server;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DateUtilTest {
    @Test
    public void test() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("actid", "1");
        jsonMap.put("userid", "1");
        jsonMap.put("token", "1");
        jsonMap.put("timestamp", "1");
        jsonMap.put("versign", "1");

        String json = JSONUtil.toJsonPrettyStr(jsonMap);
        log.debug("json=->{}", json);

//        Date date1 = DateUtil.date();
//        long times = date1.getTime();
//        String st = String.valueOf(date1.getTime());
//        log.debug("diff:{}", st.substring(0,st.length()-3));
//        Date date2 = DateUtil.parseDate("2021-04-08 12:00:00");
//        long diff = DateUtil.between(date1, date2, DateUnit.SECOND, false);
//        log.debug("diff:{}", diff);
    }

    @Test
    public void testDay() {
        Date now = DateUtil.date();
        System.out.println(now);
    }

    @Test
    public void testTimeCopy() {
        Date begin = DateUtil.parse("2021-08-16 00:00:00", "yyyy-MM-dd HH:mm:ss");

        Date startTime = DateUtil.parse("2021-07-16 15:00:00", "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateUtil.parse("2021-07-16 15:50:00", "yyyy-MM-dd HH:mm:ss");

        int dayOfWeek = DateUtil.dayOfWeek(startTime);
        log.debug("startTime:{}", startTime);
        log.debug("dayOfWeek:{}", dayOfWeek);
        Date newStartTime = DateUtil.offsetDay(begin, dayOfWeek - 2);

        newStartTime.setHours(startTime.getHours());
        newStartTime.setMinutes(startTime.getMinutes());
        newStartTime.setSeconds(startTime.getSeconds());

        log.debug("newStartTime:{}", newStartTime);
    }
}
