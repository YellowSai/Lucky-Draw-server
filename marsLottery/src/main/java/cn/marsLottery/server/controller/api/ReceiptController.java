package cn.marsLottery.server.controller.api;

import cn.jzcscw.commons.annotation.ApiAuth;
import cn.jzcscw.commons.core.Result;
import cn.marsLottery.server.dto.ReceiptAddDTO;
import cn.marsLottery.server.po.LotteryLog;
import cn.marsLottery.server.po.Receipt;
import cn.marsLottery.server.po.User;
import cn.marsLottery.server.service.LotteryLogService;
import cn.marsLottery.server.service.ReceiptService;
import cn.marsLottery.server.service.UserService;
import cn.marsLottery.server.web.WebContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 表[receipt]对应的前台类
 *
 * @author auto
 */

@Slf4j
@RestController
@RequestMapping("/api/receipt")
@Api(tags = {"收货信息接口"})
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private LotteryLogService lotteryLogService;

    @Autowired
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;

    @ApiOperation(value = "收货信息-增加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receiptAddDTO", value = "", required = true, dataType = "ReceiptAddDTO"),
    })
    @PostMapping("/add")
    public Result<String> add(@RequestBody @Validated ReceiptAddDTO receiptAddDTO) {
        RLock lock = null;
        try{
            lock = redissonClient.getLock("receipt_" + receiptAddDTO.getLotteryLogId());
            lock.lock();
            LotteryLog lotteryLog = lotteryLogService.getById(receiptAddDTO.getLotteryLogId());
            if (lotteryLog == null) {
                return Result.error("抽奖记录不存在");
            }

            User user = userService.getById(WebContext.getUserId());
            if (lotteryLog.getCustomerId() != user.getCustomerId()){
                return Result.error("非法提交");
            }

            Receipt receipt = receiptService.getByLotteryLogId(lotteryLog.getId());
            if (receipt == null){
                receipt = receiptAddDTO.toReceipt(0);
                receipt.setCustomerId(user.getCustomerId());
                receiptService.save(receipt);
                lotteryLog.setIsFill("Y");
                lotteryLogService.updateById(lotteryLog);
                return Result.ok("添加收货信息成功");
            }
            else {
                receipt = receiptAddDTO.toReceipt(receipt.getId());
                receipt.setCustomerId(user.getCustomerId());
                receiptService.updateById(receipt);
                return Result.ok("更新收货信息成功");
            }

        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @ApiOperation(value = "收货信息-获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lotteryLogId", value = "中奖记录id", required = true, dataType = "int")
    })
    @PostMapping("/detail")
    public Result<Receipt> detail(@RequestParam(defaultValue = "0", required = false) int lotteryLogId) {
        Receipt receipt = receiptService.getByLotteryLogId(lotteryLogId);
        if (receipt == null) {
            return Result.error("收货信息不存在");
        }
        return Result.data(receipt);
    }

    @ApiOperation(value = "过往收货信息-获取")
    @GetMapping("/oldDetail")
    public Result<Receipt> oldDetail(@RequestParam(defaultValue = "0", required = false) int lotteryLogId) {
        User user = userService.getById(WebContext.getUserId());

        LotteryLog lotteryLog = lotteryLogService.getById(lotteryLogId);
        if (lotteryLog.getCustomerId() != user.getCustomerId()){
            return Result.error("非法访问");
        }

        Receipt receipt = receiptService.getByLotteryLogId(lotteryLogId);
        if (receipt == null) {
            receipt = receiptService.getByCustomerId(user.getCustomerId());
            if (receipt == null){
                return Result.ok();
            }
        }
        return Result.data(receipt);
    }
}
