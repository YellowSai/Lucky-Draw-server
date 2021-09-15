package cn.marsLottery.server.service;

import cn.marsLottery.server.po.Customer;
import cn.marsLottery.server.po.LotteryCustomer;
import cn.marsLottery.server.po.User;
import cn.marsLottery.server.vo.UserVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * 表[user]对应的服务类
 *
 * @author auto
 */

public interface UserService extends IService<User> {

    String createJWT(User user);

    User getByOpenId(String openId);

    User getByCustomerId(int customerId);

    Page<User> pageBy(String customerNumber, Page page);

    List<UserVO> toListVO(List<User> userList);
}
