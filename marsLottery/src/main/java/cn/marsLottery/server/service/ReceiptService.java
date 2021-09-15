package cn.marsLottery.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.marsLottery.server.po.Receipt;

/**
* 表[receipt]对应的服务类
*
* @author auto
*/

public interface ReceiptService extends IService<Receipt> {
    Receipt getByLotteryLogId (int lotteryLogId);

    Receipt getByCustomerId (int customerId);
}
