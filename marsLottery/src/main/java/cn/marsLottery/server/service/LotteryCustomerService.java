package cn.marsLottery.server.service;

import cn.marsLottery.server.po.Lottery;
import cn.marsLottery.server.vo.LotteryCustomerVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.marsLottery.server.po.LotteryCustomer;

import java.util.List;

/**
* 表[lottery_customer]对应的服务类
*
* @author auto
*/

public interface LotteryCustomerService extends IService<LotteryCustomer> {

    Page<LotteryCustomer> pageBy(int lotteryId, String number, Page page);

    List<LotteryCustomerVO> toListVO(List<LotteryCustomer> lotteryCustomers);

    LotteryCustomer getByLotteryIdAndCustomerId(int lotteryId,int customerId);

    LotteryCustomer getByCustomerId(int customerId);

    List<LotteryCustomer> listByIdIn(List<Integer> customerIdList);
}
