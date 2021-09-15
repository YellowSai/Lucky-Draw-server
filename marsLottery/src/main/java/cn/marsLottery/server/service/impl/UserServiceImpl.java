package cn.marsLottery.server.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.jzcscw.commons.util.JWTUtil;
import cn.marsLottery.server.config.AppConfig;
import cn.marsLottery.server.po.Customer;
import cn.marsLottery.server.po.Lottery;
import cn.marsLottery.server.po.LotteryCustomer;
import cn.marsLottery.server.service.CustomerService;
import cn.marsLottery.server.vo.LotteryCustomerVO;
import cn.marsLottery.server.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.marsLottery.server.dao.UserDao;
import cn.marsLottery.server.po.User;
import cn.marsLottery.server.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* User对应的服务类实现
*
* @author auto
*/

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private CustomerService customerService;

    @Override
    public String createJWT(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        String token = JWTUtil.createJWT(claims, appConfig.getJwtSubject(), appConfig.getJwtSecret(), appConfig.getJwtLife());
        return token;
    }

    @Override
    public User getByOpenId(String openId) {
        return this.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenId,openId));
    }

    @Override
    public User getByCustomerId(int customerId) {
        return this.getOne(new LambdaQueryWrapper<User>().eq(User::getCustomerId,customerId));
    }

    @Override
    public Page<User> pageBy(String customerNumber, Page page) {

        return this.getBaseMapper().pageBy(customerNumber, page);
    }

    @Override
    public List<UserVO> toListVO(List<User> userList) {
        if (CollectionUtil.isEmpty(userList)){
            return new ArrayList<>();
        }

        List<Integer> customerIdList  = userList.stream().map(User::getCustomerId).distinct().collect(Collectors.toList());
        List<Customer> customerList = customerService.listByIdIn(customerIdList);
        Map<Integer, Customer> customerMap = customerList.stream().collect(Collectors.toMap(Customer::getId, a -> a));

        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList){
            UserVO userVO = new UserVO(user);
            userVO.setCustomer(customerMap.get(user.getCustomerId()));
            userVOList.add(userVO);
        }
        return userVOList;
    }
}
