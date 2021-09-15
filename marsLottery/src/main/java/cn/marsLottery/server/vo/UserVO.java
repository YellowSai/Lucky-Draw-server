package cn.marsLottery.server.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.marsLottery.server.po.Customer;
import cn.marsLottery.server.po.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 表[user]对应VO类
 *
 * @author auto
 */

@Data
public class UserVO {

    /**
     * 自增id
     */
    @ApiModelProperty(value = "自增id", dataType = "int")
    private int id;

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息", dataType = "Customer")
    private Customer customer;

    /**
     * openId
     */
    @ApiModelProperty(value = "openId", dataType = "String")
    private String openId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", dataType = "Date")
    private Date createTime;


    public UserVO(User user) {
        if (user != null) {
            BeanUtil.copyProperties(user, this);
        }
    }
}
