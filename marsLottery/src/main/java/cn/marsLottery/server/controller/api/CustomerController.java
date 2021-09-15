package cn.marsLottery.server.controller.api;


import cn.jzcscw.commons.annotation.ApiAuth;
import cn.jzcscw.commons.core.Result;
import cn.marsLottery.server.po.Customer;
import cn.marsLottery.server.po.LotteryCustomer;
import cn.marsLottery.server.po.User;
import cn.marsLottery.server.service.CustomerService;
import cn.marsLottery.server.service.LotteryCustomerService;
import cn.marsLottery.server.service.UserService;
import cn.marsLottery.server.vo.CustomerVO;
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

import java.util.Objects;

/**
 * 表[customer]对应的前台类
 *
 * @author auto
 */

@Slf4j
@RestController
@RequestMapping("/api/customer")
@Api(tags = {"客户接口"})
public class CustomerController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LotteryCustomerService lotteryCustomerService;

    @ApiOperation(value = "客户-验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lotteryId", value = "活动id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "customerNumber", value = "客户编号", required = true, dataType = "String"),
    })
    @GetMapping("/verify")
    public Result<CustomerVO> list(@RequestParam(defaultValue = "0", required = false) int lotteryId,
                                   @RequestParam(defaultValue = "", required = false) String customerNumber) {
        if (Objects.equals(customerNumber, "")) {
            return Result.error("未查询到您的编号信息，如有疑问，请于对应的玛氏销售确认");
        }
        if (lotteryId <= 0) {
            return Result.error("活动不存在");
        }
        Customer customer = customerService.getByNumber(customerNumber);
        if (customer == null) {
            return Result.error("未查询到您的编号信息，如有疑问，请于对应的玛氏销售确认");
        }
        User user = userService.getByCustomerId(customer.getId());
        if (user != null){
            return Result.error("客户编号已被注册");
        }
        LotteryCustomer lotteryCustomer = lotteryCustomerService.getByLotteryIdAndCustomerId(lotteryId, customer.getId());
        if (lotteryCustomer == null) {
            return Result.error("不在本次活动的参与范围");
        }
        return Result.data(new CustomerVO(customer));
    }
}
