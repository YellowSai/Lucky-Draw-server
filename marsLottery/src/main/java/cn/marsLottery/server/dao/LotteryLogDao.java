package cn.marsLottery.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.marsLottery.server.po.LotteryLog;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
* 表[lottery_log]对应的实体类
*
* @author auto
*
*/

public interface LotteryLogDao extends BaseMapper<LotteryLog> {
    Page<LotteryLog> pageBy(@Param("lotteryId")int lotteryId,
                            @Param("isWinner")String isWinner,
                            @Param("number")String number,
                            @Param("isDispatched")String isDispatched,
                            @Param("page")Page page);
}
