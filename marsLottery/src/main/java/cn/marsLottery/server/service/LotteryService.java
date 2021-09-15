package cn.marsLottery.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.marsLottery.server.po.Lottery;

import java.util.List;

/**
* 表[lottery]对应的服务类
*
* @author auto
*/

public interface LotteryService extends IService<Lottery> {

    List<Lottery> listByIdIn(List<Integer> lotteryIdList);

    Lottery getByDateRange();

}
