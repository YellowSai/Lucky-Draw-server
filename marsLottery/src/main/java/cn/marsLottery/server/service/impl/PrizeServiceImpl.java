package cn.marsLottery.server.service.impl;

import cn.marsLottery.server.dao.PrizeDao;
import cn.marsLottery.server.po.Prize;
import cn.marsLottery.server.service.PrizeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Prize对应的服务类实现
 *
 * @author auto
 */

@Service
public class PrizeServiceImpl extends ServiceImpl<PrizeDao, Prize> implements PrizeService {

    @Override
    public Map<Integer, Prize> getPrizeMap(List<Integer> prizeIdList) {
        if (prizeIdList == null || prizeIdList.size() == 0) {
            return new HashMap<>();
        }
        List<Prize> prizeList = listByIds(prizeIdList);
        Map<Integer, Prize> prizeMap = prizeList.stream().collect(Collectors.toMap(Prize::getId, a -> a));
        return prizeMap;
    }

    @Override
    public List<Prize> listByIdIn(List<Integer> prizeIdList) {
        if (prizeIdList == null || prizeIdList.size() == 0) {
            return new ArrayList<>();
        }
        List<Prize> prizeList = listByIds(prizeIdList);
        return prizeList;
    }

    @Override
    public Prize getByLotteryIdAndAward(int lotteryId, int award) {
        return this.getOne(new LambdaQueryWrapper<Prize>().eq(Prize::getLotteryId, lotteryId).eq(Prize::getAward, award));
    }

    @Override
    public List<Prize> getListByLotteryId(int lotteryId) {
        return this.list(new LambdaQueryWrapper<Prize>().eq(Prize::getLotteryId, lotteryId));
    }
}
