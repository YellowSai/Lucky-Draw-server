package cn.marsLottery.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.marsLottery.server.po.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
* 表[user]对应的实体类
*
* @author auto
*
*/

public interface UserDao extends BaseMapper<User> {
    Page<User> pageBy(@Param("customerNumber") String customerNumber,
                         @Param("pg") Page page);
}
