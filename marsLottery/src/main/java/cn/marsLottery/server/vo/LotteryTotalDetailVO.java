package cn.marsLottery.server.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class LotteryTotalDetailVO {

    private String name;
    private Date startTime;
    private Date endTime;
    private Date currentTime;
    private String schedule;

    private List<Map<String ,Object>> overallSituation;

    private List<Map<String, Object>> winningSituation;

    private List<Map<String,Object>> todayWinningSituation;
}
