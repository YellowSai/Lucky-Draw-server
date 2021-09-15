package cn.marsLottery.server.service.impl;

import cn.marsLottery.server.dao.ReceiptDao;
import cn.marsLottery.server.po.Receipt;
import cn.marsLottery.server.service.ReceiptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Receipt对应的服务类实现
 *
 * @author auto
 */

@Service
public class ReceiptServiceImpl extends ServiceImpl<ReceiptDao, Receipt> implements ReceiptService {

    @Override
    public Receipt getByLotteryLogId(int lotteryLogId) {
        return this.getOne(new LambdaQueryWrapper<Receipt>().eq(Receipt::getLotteryLogId, lotteryLogId));
    }

    @Override
    public Receipt getByCustomerId(int customerId) {
        return this.getOne(new LambdaQueryWrapper<Receipt>().eq(Receipt::getCustomerId, customerId).orderByDesc(Receipt::getId).last("limit 1"));
    }
}
