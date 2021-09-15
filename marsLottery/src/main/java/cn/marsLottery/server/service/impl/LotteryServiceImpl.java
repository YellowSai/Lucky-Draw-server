package cn.marsLottery.server.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.marsLottery.server.dao.LotteryDao;
import cn.marsLottery.server.po.Lottery;
import cn.marsLottery.server.service.LotteryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Lottery对应的服务类实现
 *
 * @author auto
 */

@Service
public class LotteryServiceImpl extends ServiceImpl<LotteryDao, Lottery> implements LotteryService {

    @Override
    public List<Lottery> listByIdIn(List<Integer> lotteryIdList) {
        if (lotteryIdList == null || lotteryIdList.size() == 0) {
            return new ArrayList<>();
        }
        List<Lottery> lotteryList = listByIds(lotteryIdList);
        return lotteryList;
    }

    @Override
    public Lottery getByDateRange() {
        Date date = DateUtil.date();
        List<Lottery> lotteryList = this.list(new LambdaQueryWrapper<Lottery>()
                .orderByAsc(Lottery::getStartTime)
                .le(Lottery::getStartTime, date)
                .ge(Lottery::getEndTime, date)
                .eq(Lottery::getIsStart, "Y"));

        long dateTimeStamp = date.getTime();
        long shortestTimeDistance = 0;
        Lottery lottery = new Lottery();
        for (int i = 0, length = lotteryList.size(); i < length; i++) {
            long startTime = lotteryList.get(i).getStartTime().getTime();
            long timeDistance = dateTimeStamp - startTime;
            if (shortestTimeDistance == 0) {
                shortestTimeDistance = dateTimeStamp - startTime;
                lottery = lotteryList.get(i);
                continue;
            }
            if (shortestTimeDistance > timeDistance) {
                shortestTimeDistance = timeDistance;
                lottery = lotteryList.get(i);
            }
        }
        return lottery;
    }
}
