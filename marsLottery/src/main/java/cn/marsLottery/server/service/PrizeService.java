package cn.marsLottery.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.marsLottery.server.po.Prize;

import java.util.List;
import java.util.Map;

/**
* 表[prize]对应的服务类
*
* @author auto
*/

public interface PrizeService extends IService<Prize> {
    Map<Integer, Prize> getPrizeMap(List<Integer> prizeIdList);

    List<Prize> listByIdIn(List<Integer> prizeIdList);

    Prize getByLotteryIdAndAward(int lotteryId,int award);

    List<Prize> getListByLotteryId(int lotteryId);
}
