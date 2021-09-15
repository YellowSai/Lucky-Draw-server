package cn.marsLottery.server.controller.admapi;

import cn.jzcscw.commons.constant.YesNoStatus;
import cn.marsLottery.server.dto.ReceiptAddDTO;
import cn.marsLottery.server.dto.ReceiptUpdateDTO;
import cn.marsLottery.server.po.Customer;
import cn.marsLottery.server.po.LotteryLog;
import cn.marsLottery.server.po.Receipt;
import cn.marsLottery.server.service.CustomerService;
import cn.marsLottery.server.service.LotteryLogService;
import cn.marsLottery.server.service.ReceiptService;
import cn.marsLottery.server.service.SmsService;
import cn.marsLottery.server.vo.ReceiptVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import cn.jzcscw.commons.core.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 表[receipt]对应的后台管理类
 *
 * @author auto
 */

@Slf4j
@RestController
@RequestMapping("/admapi/receipt")
@Api(tags = {"收货信息管理接口"})
public class ReceiptAdminController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LotteryLogService lotteryLogService;

    @ApiOperation(value = "收货信息-列表")
    @GetMapping("/list")
    public Result<Page<Receipt>> list(@RequestParam(defaultValue = "0", required = false) int lotteryLogId,
                                      Page page) {
        LambdaQueryWrapper<Receipt> queryWrapper = new LambdaQueryWrapper<>();
        if (lotteryLogId > 0) {
            queryWrapper.eq(Receipt::getLotteryLogId, lotteryLogId);
        }
        Page<Receipt> pageData = receiptService.page(page, queryWrapper);
        return Result.data(pageData);
    }

    @ApiOperation(value = "收货信息-详情")
    @GetMapping("/detail")
    public Result<ReceiptVO> detail(int id) {
        if (id <= 0) {
            return Result.error("收货信息不存在");
        }
        Receipt receipt = receiptService.getById(id);
        if (receipt == null) {
            return Result.error("收货信息不存在");
        }
        ReceiptVO receiptVO = new ReceiptVO(receipt);
        return Result.data(receiptVO);
    }

    @ApiOperation(value = "收货信息-详情")
    @GetMapping("/detailByLotteryLog")
    public Result<ReceiptVO> detailByLotteryLog(int lotteryLogId) {
        if (lotteryLogId <= 0) {
            return Result.error("收货信息不存在");
        }
        Receipt receipt = receiptService.getByLotteryLogId(lotteryLogId);
        if (receipt == null) {
            return Result.error("还未填写收货信息");
        }
        ReceiptVO receiptVO = new ReceiptVO(receipt);
        return Result.data(receiptVO);
    }

    @ApiOperation(value = "收货信息-更新")
    @PutMapping("/update")
    public Result<String> update(@RequestBody @Validated ReceiptUpdateDTO receiptUpdateDTO) {
        Receipt receipt = receiptService.getById(receiptUpdateDTO.getId());
        if (receipt == null) {
            return Result.error("收货信息不存在");
        }
        receipt.setDeliveryName(receiptUpdateDTO.getDeliveryName());
        receipt.setDeliveryNum(receiptUpdateDTO.getDeliveryNum());
        receiptService.updateById(receipt);

        LotteryLog lotteryLog = lotteryLogService.getById(receiptUpdateDTO.getLotteryLogId());
        if (lotteryLog == null) {
            return Result.error("发货失败");
        }
        lotteryLog.setIsDispatched(YesNoStatus.YES.getValue());
        lotteryLogService.updateById(lotteryLog);

        String content = "【玛氏箭牌】感谢您参加专属经销商百万俱乐部活动！您的奖品已通过【" +
                receiptUpdateDTO.getDeliveryName() +
                "】发货，快递单号为：" + receiptUpdateDTO.getDeliveryNum() +
                "，请注意查收。疑问请致电：400-012-8206（工作日：10：00-18：00）";
        Customer customer = customerService.getById(lotteryLog.getCustomerId());
        smsService.sendSms(content, customer.getMobile(),5000);

        return Result.ok("发货成功");
    }
}
