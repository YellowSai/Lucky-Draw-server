package cn.marsLottery.server.service;

import cn.jzcscw.commons.core.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.marsLottery.server.po.Customer;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
* 表[customer]对应的服务类
*
* @author auto
*/

public interface CustomerService extends IService<Customer> {

    void importExcel(File excel, int lotteryId);

    List<Customer> listByIdIn(List<Integer> customerIdList);

    Customer getByNumber(String number);

    Customer getByMobile(String mobile);

    Customer getByCustomerNumberAndMobile(String customerNumber,String mobile);

    Map<Integer, Customer> getCustomerMap(List<Integer> customerIdList);
}
