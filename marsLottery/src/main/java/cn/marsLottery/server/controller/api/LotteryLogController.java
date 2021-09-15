package cn.marsLottery.server.controller.api;

import cn.jzcscw.commons.core.Result;
import cn.marsLottery.server.po.Customer;
import cn.marsLottery.server.po.LotteryLog;
import cn.marsLottery.server.po.Prize;
import cn.marsLottery.server.po.User;
import cn.marsLottery.server.service.CustomerService;
import cn.marsLottery.server.service.LotteryLogService;
import cn.marsLottery.server.service.PrizeService;
import cn.marsLottery.server.service.UserService;
import cn.marsLottery.server.vo.LotteryLogVO;
import cn.marsLottery.server.vo.PrizeVO;
import cn.marsLottery.server.web.WebContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 表[lottery_log]对应的前台类
 *
 * @author auto
 */

@Slf4j
@RestController
@RequestMapping("/api/lotteryLog")
@Api(tags = {"抽奖记录接口"})
public class LotteryLogController {

    @Autowired
    private UserService userService;

    @Autowired
    private PrizeService prizeService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LotteryLogService lotteryLogService;

    @ApiOperation(value = "抽奖记录-列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lotteryId", value = "活动id", required = true, dataType = "int")
    })
    @GetMapping("/list")
    public Result<List<LotteryLogVO>> list(@RequestParam(defaultValue = "0", required = false) int lotteryId) {
        User user = userService.getById(WebContext.getUserId());
        Customer customer = customerService.getById(user.getCustomerId());
        if (customer == null) {
            return Result.error("客户信息不存在");
        }

        List<LotteryLog> lotteryLogList = lotteryLogService.getListByLotteryIdAndCustomerIdAndWinner(lotteryId, customer.getId());
        List<LotteryLogVO> lotteryLogVOList = new ArrayList<>();
        for (LotteryLog lotteryLog : lotteryLogList) {
            LotteryLogVO lotteryLogVO = new LotteryLogVO(lotteryLog);
            Prize prize = prizeService.getById(lotteryLog.getPrizeId());
            lotteryLogVO.setPrize(new PrizeVO(prize));
            lotteryLogVOList.add(lotteryLogVO);
        }
        return Result.data(lotteryLogVOList);
    }
}
