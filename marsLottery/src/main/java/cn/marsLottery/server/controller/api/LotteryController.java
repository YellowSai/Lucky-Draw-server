package cn.marsLottery.server.controller.api;

import cn.jzcscw.commons.annotation.ApiAuth;
import cn.jzcscw.commons.core.Result;
import cn.marsLottery.server.po.*;
import cn.marsLottery.server.service.*;
import cn.marsLottery.server.vo.LotteryLogVO;
import cn.marsLottery.server.vo.LotteryVO;
import cn.marsLottery.server.vo.PrizeVO;
import cn.marsLottery.server.web.WebContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 表[lottery]对应的前台类
 *
 * @author auto
 */

@Slf4j
@RestController
@RequestMapping("/api/lottery")
@Api(tags = {"抽奖活动接口"})
public class LotteryController {
    @Autowired
    private PrizeService prizeService;

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private UserService userService;

    @Autowired
    private LotteryCustomerService lotteryCustomerService;

    @Autowired
    private LotteryLogService lotteryLogService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SmsService smsService;

    @Resource
    private RedissonClient redissonClient;

    @ApiOperation(value = "抽奖活动-信息", notes = "返回开启的活动的信息,不需要参数")
    @GetMapping("/info")
    @ApiAuth(ignore = true)
    public Result<LotteryVO> info(@RequestParam(defaultValue = "0", required = false) int lotteryId) {
        if (lotteryId == 0){
            Lottery lottery = lotteryService.getByDateRange();
            if (lottery == null) {
                return Result.error("没有开启的活动");
            }
            LotteryVO lotteryVO = new LotteryVO(lottery);
            return Result.data(lotteryVO);
        }
        Lottery lottery = lotteryService.getById(lotteryId);
        if (lottery == null) {
            return Result.error("活动不存在");
        }
        Date date = new Date();
        if (!Objects.equals(lottery.getIsStart(), "Y")) {
            return Result.error("活动未开启");
        }
        if (!(lottery.getStartTime().getTime() < date.getTime() && date.getTime() < lottery.getEndTime().getTime())) {
            return Result.error("不在活动时间范围内");
        }
        LotteryVO lotteryVO = new LotteryVO(lottery);
        return Result.data(lotteryVO);
    }

    @ApiOperation(value = "抽奖活动-抽奖次数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lotteryId", value = "活动id", required = true, dataType = "int"),
    })
    @GetMapping("/drawTimes")
    public Result<Integer> drawTimes(@RequestParam(defaultValue = "0", required = false) int lotteryId) {
        User user = userService.getById(WebContext.getUserId());

        LotteryCustomer lotteryCustomer = lotteryCustomerService.getByLotteryIdAndCustomerId(lotteryId, user.getCustomerId());
        if (lotteryCustomer == null) {
            return Result.error("不存在");
        }
        int customerDrawTimes = lotteryCustomer.getDrawsTimes();
        List<LotteryLog> lotteryLogList = lotteryLogService.getListByLotteryIdAndCustomerId(lotteryId, user.getCustomerId());
        for (LotteryLog lotteryLog : lotteryLogList) {
            customerDrawTimes -= 1;
        }
        return Result.data(customerDrawTimes);
    }

    @ApiOperation(value = "抽奖活动-抽奖")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lotteryId", value = "活动id", required = true, dataType = "int"),
    })
    @GetMapping("/draw")
    public Result<LotteryLogVO> draw(@RequestParam(defaultValue = "0", required = false) int lotteryId) {
        User user = userService.getById(WebContext.getUserId());

        RLock lock = null;
        try {
            lock = redissonClient.getLock("lottery_draw_" + user.getId());
            lock.lock();
            Lottery lottery = lotteryService.getById(lotteryId);
            if (lottery == null) {
                return Result.error("活动不存在");
            }
            Date date = new Date();
            if (!Objects.equals(lottery.getIsStart(), "Y")) {
                return Result.error("活动未开启");
            }
            if (!(lottery.getStartTime().getTime() < date.getTime() && date.getTime() < lottery.getEndTime().getTime())) {
                return Result.error("不在活动时间范围内");
            }
            // 检查用户是否在抽奖名单中
            LotteryCustomer lotteryCustomer = lotteryCustomerService.getByLotteryIdAndCustomerId(lotteryId, user.getCustomerId());
            if (lotteryCustomer == null) {
                return Result.error("不存在");
            }
            int[] customerDrawTimes = {
                    lotteryCustomer.getDrawsTimes(),
                    lotteryCustomer.getFirstPrize(),
                    lotteryCustomer.getSecondPrize(),
                    lotteryCustomer.getThirdPrize(),
                    lotteryCustomer.getFourthPrize()
            };

            // 查询抽奖记录
            List<LotteryLog> lotteryLogList = lotteryLogService.getListByLotteryIdAndCustomerId(lotteryId, user.getCustomerId());
            for (LotteryLog lotteryLog : lotteryLogList) {
                if (lotteryLog.getPrizeId() != 0) {
                    Prize prize = prizeService.getById(lotteryLog.getPrizeId());
                    customerDrawTimes[prize.getAward()] -= 1;
                }
            }
            for (int i = 1; i < customerDrawTimes.length; i++) {
                if (customerDrawTimes[i] != 0) {
                    //中奖，写入log
                    Prize prize = prizeService.getByLotteryIdAndAward(lotteryId, i);
                    if (prize == null) {
                        return Result.error("未配置对应奖项，请联系后台管理员");
                    }
                    LotteryLog lotteryLog = new LotteryLog();
                    lotteryLog.setLotteryId(lotteryId);
                    lotteryLog.setCustomerId(user.getCustomerId());
                    lotteryLog.setPrizeId(prize.getId());
                    lotteryLog.setIsWinner("Y");
                    lotteryLog.setDataStatus(2);
                    lotteryLogService.save(lotteryLog);

                    Customer customer = customerService.getById(user.getCustomerId());
                    String content = "【玛氏箭牌】感谢您参加专属经销商百万俱乐部活动！恭喜您获得 " + prize.getName() + " 一份，我们将会在7个工作日内安排发奖，物流信息将会在奖品发出后通过短信方式告知，请注意查收短信。疑问请致电：400-012-8206（工作日：10：00-18：00）";
                    smsService.sendSms(content, customer.getMobile(),5000);

                    lotteryLog = lotteryLogService.getByLotteryIdAndCustomerIdAndWinner(lotteryId, user.getCustomerId(), "Y");
                    LotteryLogVO lotteryLogVO = new LotteryLogVO(lotteryLog);
                    lotteryLogVO.setPrize(new PrizeVO(prize));
                    return Result.data(lotteryLogVO);

                }
            }
            if (customerDrawTimes[0] > lotteryLogList.size()) {
                //不中奖，写入log
                LotteryLog lotteryLog = new LotteryLog();
                lotteryLog.setLotteryId(lotteryId);
                lotteryLog.setCustomerId(user.getCustomerId());
                lotteryLog.setPrizeId(0);
                lotteryLog.setIsWinner("N");
                lotteryLog.setDataStatus(2);
                lotteryLogService.save(lotteryLog);
                lotteryLog = lotteryLogService.getByLotteryIdAndCustomerIdAndWinner(lotteryId, user.getCustomerId(), "N");
                LotteryLogVO lotteryLogVO = new LotteryLogVO(lotteryLog);
                Prize prize = new Prize();
                lotteryLogVO.setPrize(new PrizeVO(prize));
                return Result.data(lotteryLogVO);
            } else {
                return Result.error("您的抽奖次数为0");
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }
}
