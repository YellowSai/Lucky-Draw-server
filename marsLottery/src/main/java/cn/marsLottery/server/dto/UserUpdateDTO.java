package cn.marsLottery.server.dto;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.marsLottery.server.po.User;

    import java.util.Date;
/**
* 表[user]对应UpdateDTO类
*
* @author auto
*
*/

@Data
public class UserUpdateDTO {

    /**
    * 自增id
    */
    @ApiModelProperty(value = "自增id", dataType = "int")
    private int id;

    /**
    * 用户id
    */
    @ApiModelProperty(value = "用户id", dataType = "int")
    private int customerId;

    /**
    * 微信appId
    */
    @ApiModelProperty(value = "微信appId", dataType = "String")
    private String appId;

    /**
    * 在appId下的openId
    */
    @ApiModelProperty(value = "在appId下的openId", dataType = "String")
    private String openId;

    /**
    * 类型，office-公众号,miniprogram-小程序，app-app
    */
    @ApiModelProperty(value = "类型，office-公众号,miniprogram-小程序，app-app", dataType = "String")
    private String type;

    /**
    * 微信unionid
    */
    @ApiModelProperty(value = "微信unionid", dataType = "String")
    private String unionId;

    /**
    * 当前绑定
    */
    @ApiModelProperty(value = "当前绑定", dataType = "String")
    private String isBind;

    /**
    * 是否订阅
    */
    @ApiModelProperty(value = "是否订阅", dataType = "String")
    private String isSubscribe;

    /**
    * 微信昵称
    */
    @ApiModelProperty(value = "微信昵称", dataType = "String")
    private String nickname;

    /**
    * 微信头像
    */
    @ApiModelProperty(value = "微信头像", dataType = "String")
    private String avatar;

    /**
    * 用户信息
    */
    @ApiModelProperty(value = "用户信息", dataType = "String")
    private String data;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间", dataType = "Date")
    private Date createTime;
    public User toUser() {
        User user = new User();
        BeanUtil.copyProperties(this, user);
        return user;
    }
}
