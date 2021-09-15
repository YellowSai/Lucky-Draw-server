package cn.marsLottery.server.controller.admapi;

import cn.jzcscw.commons.core.Result;
import cn.marsLottery.server.dto.LotteryCustomerAddDTO;
import cn.marsLottery.server.dto.LotteryCustomerUpdateDTO;
import cn.marsLottery.server.po.Customer;
import cn.marsLottery.server.po.LotteryCustomer;
import cn.marsLottery.server.service.CustomerService;
import cn.marsLottery.server.service.LotteryCustomerService;
import cn.marsLottery.server.vo.LotteryCustomerVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 表[lottery_customer]对应的后台管理类
 *
 * @author auto
 */

@Slf4j
@RestController
@RequestMapping("/admapi/lotteryCustomer")
@Api(tags = {"客户抽奖管理接口"})
public class LotteryCustomerAdminController {

    @Autowired
    private LotteryCustomerService lotteryCustomerService;

    @Autowired
    private CustomerService customerService;

    @ApiOperation(value = "客户抽奖-列表")
    @GetMapping("/list")
    public Result<Page<LotteryCustomerVO>> list(@RequestParam(defaultValue = "0", required = false) int lotteryId,
                                                @RequestParam(defaultValue = "", required = false) String number,
                                                Page page) {
        Page<LotteryCustomer> pageData = lotteryCustomerService.pageBy(lotteryId, number, page);

        List<LotteryCustomerVO> lotteryCustomerVOList = lotteryCustomerService.toListVO(pageData.getRecords());

        Page<LotteryCustomerVO> lotteryCustomerVOPage = new Page<>();
        lotteryCustomerVOPage.setTotal(pageData.getTotal());
        lotteryCustomerVOPage.setRecords(lotteryCustomerVOList);
        lotteryCustomerVOPage.setCurrent(page.getCurrent());
        lotteryCustomerVOPage.setSize(page.getSize());

        return Result.data(lotteryCustomerVOPage);
    }

    @ApiOperation(value = "客户抽奖-详情")
    @GetMapping("/detail")
    public Result<LotteryCustomerVO> detail(int id) {
        if (id <= 0) {
            return Result.error("客户抽奖不存在");
        }
        LotteryCustomer lotteryCustomer = lotteryCustomerService.getById(id);
        if (lotteryCustomer == null) {
            return Result.error("客户抽奖不存在");
        }
        LotteryCustomerVO lotteryCustomerVO = new LotteryCustomerVO(lotteryCustomer);
        return Result.data(lotteryCustomerVO);
    }

    @ApiOperation(value = "客户抽奖-增加")
    @PostMapping("/add")
    public Result<String> add(@RequestBody @Validated LotteryCustomerAddDTO lotteryCustomerAddDTO) {
        LotteryCustomer lotteryCustomer = lotteryCustomerAddDTO.toLotteryCustomer();
        lotteryCustomerService.save(lotteryCustomer);
        return Result.ok("添加客户抽奖成功");
    }

    @ApiOperation(value = "客户抽奖-更新")
    @PutMapping("/update")
    public Result<String> update(@RequestBody @Validated LotteryCustomerUpdateDTO lotteryCustomerUpdateDTO) {
        LotteryCustomer lotteryCustomer = lotteryCustomerService.getById(lotteryCustomerUpdateDTO.getId());
        Customer customer = customerService.getById(lotteryCustomerUpdateDTO.getCustomerId());
        int total = lotteryCustomerUpdateDTO.getFirstPrize() + lotteryCustomerUpdateDTO.getSecondPrize() + lotteryCustomerUpdateDTO.getThirdPrize() + lotteryCustomerUpdateDTO.getFourthPrize() + lotteryCustomerUpdateDTO.getNotPrize();
        if (lotteryCustomer == null) {
            return Result.error("客户抽奖不存在");
        }
        if (customer == null) {
            return Result.error("客户不存在");
        }
        if (total > lotteryCustomerUpdateDTO.getDrawsTimes()) {
            return Result.error("奖品总数大于抽奖次数");
        }

        customer.setId(lotteryCustomerUpdateDTO.getCustomerId());
        customer.setDirectorName(lotteryCustomerUpdateDTO.getDirectorName());
        customer.setMobile(lotteryCustomerUpdateDTO.getMobile());
        customerService.updateById(customer);

        lotteryCustomer = lotteryCustomerUpdateDTO.toLotteryCustomer();
        lotteryCustomerService.updateById(lotteryCustomer);
        return Result.ok("编辑客户抽奖成功");
    }

    @ApiOperation(value = "客户抽奖-删除")
    @DeleteMapping("/delete")
    public Result<String> delete(int id) {
        if (id <= 0) {
            return Result.error("客户抽奖不存在");
        }
        LotteryCustomer lotteryCustomer = lotteryCustomerService.getById(id);
        if (lotteryCustomer == null) {
            return Result.error("客户抽奖不存在");
        }
        lotteryCustomerService.removeById(id);
        return Result.ok("删除客户抽奖成功");
    }
}
