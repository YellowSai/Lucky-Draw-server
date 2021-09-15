package cn.marsLottery.server.service;

import cn.marsLottery.server.po.LotteryLog;
import cn.marsLottery.server.vo.LotteryLogVO;
import cn.marsLottery.server.vo.LotteryTotalDetailVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;

/**
 * 表[lottery_log]对应的服务类
 *
 * @author auto
 */

public interface LotteryLogService extends IService<LotteryLog> {

    List<LotteryLog> getListByLotteryIdAndCustomerId(int lotteryId, int customerId);

    List<LotteryLog> getListByLotteryId(int lotteryId);

    List<LotteryLog> getListByLotteryIdAndCustomerIdAndWinner(int lotteryId, int customerId);

    LotteryLog getByLotteryIdAndCustomerIdAndWinner(int lotteryId, int customerId, String isWinner);

    List<LotteryLogVO> toListVO(List<LotteryLog> lotteryLogs);

    Page<LotteryLog> pageBy(int lotteryId, String isWinner, String number, String isDispatched, Page page);

    String exportExcel(int lotteryLogId) throws IOException;

    String exportReceiptInformation(int lotteryLogId) throws IOException;

    LotteryTotalDetailVO eventsGallery(int lotteryLogId);
}
