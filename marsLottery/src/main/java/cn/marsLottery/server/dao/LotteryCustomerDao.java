package cn.marsLottery.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.marsLottery.server.po.LotteryCustomer;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
* 表[lottery_customer]对应的实体类
*
* @author auto
*
*/

public interface LotteryCustomerDao extends BaseMapper<LotteryCustomer> {
    Page<LotteryCustomer> pageBy(@Param("lotteryId")int lotteryId,
                                 @Param("number")String number,
                                 @Param("page")Page page);
}
