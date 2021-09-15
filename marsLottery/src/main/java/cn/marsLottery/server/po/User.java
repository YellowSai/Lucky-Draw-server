package cn.marsLottery.server.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 表[user]对应实体类
 *
 * @author auto
 */

@Data
@TableName(value = "`user`", schema = "mars_lottery")
@ApiModel(value = "表user的实体类")
public class User {

    /**
     * 自增id
     */
    @ApiModelProperty(value = "自增id", dataType = "int")
    @TableField("`id`")
    @TableId(value = "`id`", type = IdType.AUTO)
    private int id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", dataType = "int")
    @TableField("`customer_id`")
    private int customerId;

    /**
     * 在appId下的openId
     */
    @ApiModelProperty(value = "在appId下的openId", dataType = "String")
    @TableField("`open_id`")
    private String openId;

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息", dataType = "String")
    @TableField("`data`")
    private String data;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", dataType = "Date")
    @TableField("`create_time`")
    private Date createTime;

}
