package cn.marsLottery.server.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.marsLottery.server.po.Customer;
import cn.marsLottery.server.po.Lottery;
import cn.marsLottery.server.service.CustomerService;
import cn.marsLottery.server.service.LotteryService;
import cn.marsLottery.server.vo.LotteryCustomerVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.marsLottery.server.dao.LotteryCustomerDao;
import cn.marsLottery.server.po.LotteryCustomer;
import cn.marsLottery.server.service.LotteryCustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* LotteryCustomer对应的服务类实现
*
* @author auto
*/

@Service
public class LotteryCustomerServiceImpl extends ServiceImpl<LotteryCustomerDao, LotteryCustomer> implements LotteryCustomerService {

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private CustomerService customerService;

    @Override
    public Page<LotteryCustomer> pageBy(int lotteryId, String number, Page page) {
        return this.getBaseMapper().pageBy(lotteryId, number, page);
    }

    @Override
    public List<LotteryCustomerVO> toListVO(List<LotteryCustomer> lotteryCustomers) {
        if (CollectionUtil.isEmpty(lotteryCustomers)){
            return new ArrayList<>();
        }

        List<Integer> lotteryIdList = lotteryCustomers.stream().map(LotteryCustomer::getLotteryId).distinct().collect(Collectors.toList());
        List<Lottery> lotteryList = lotteryService.listByIdIn(lotteryIdList);
        Map<Integer, Lottery> lotteryVOMap = lotteryList.stream().collect(Collectors.toMap(Lottery::getId, a -> a));

        List<Integer> customerIdList = lotteryCustomers.stream().map(LotteryCustomer::getCustomerId).distinct().collect(Collectors.toList());
        List<Customer> customerList = customerService.listByIdIn(customerIdList);
        Map<Integer, Customer> customerVOMap = customerList.stream().collect(Collectors.toMap(Customer::getId, a -> a));

        List<LotteryCustomerVO> lotteryCustomerVOList = new ArrayList<>();
        for (LotteryCustomer lotteryCustomer : lotteryCustomers){
            LotteryCustomerVO lotteryCustomerVO = new LotteryCustomerVO(lotteryCustomer);
            lotteryCustomerVO.setLottery(lotteryVOMap.get(lotteryCustomer.getLotteryId()));
            lotteryCustomerVO.setCustomer(customerVOMap.get(lotteryCustomer.getCustomerId()));
            lotteryCustomerVOList.add(lotteryCustomerVO);
        }
        return lotteryCustomerVOList;
    }

    @Override
    public LotteryCustomer getByLotteryIdAndCustomerId(int lotteryId,int customerId) {
        return this.getOne(new LambdaQueryWrapper<LotteryCustomer>().eq(LotteryCustomer::getCustomerId,customerId).eq(LotteryCustomer::getLotteryId,lotteryId));
    }

    @Override
    public LotteryCustomer getByCustomerId(int customerId) {

        return this.getOne(new LambdaQueryWrapper<LotteryCustomer>().eq(LotteryCustomer::getCustomerId,customerId));
    }

    @Override
    public List<LotteryCustomer> listByIdIn(List<Integer> customerIdList) {
        if (customerIdList == null || customerIdList.size() == 0) {
            return new ArrayList<>();
        }
        List<LotteryCustomer> lotteryList = listByIds(customerIdList);
        return lotteryList;
    }
}
