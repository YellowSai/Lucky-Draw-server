package cn.marsLottery.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
  import java.util.Date;
import lombok.Data;

/**
* 表[receipt]对应实体类
*
* @author auto
*
*/

@Data
@TableName(value = "`receipt`", schema="mars_lottery")
@ApiModel(value = "表receipt的实体类")
public class Receipt {

    /**
    * 
    */
    @ApiModelProperty(value = "", dataType = "int")
    @TableField("`id`")
    @TableId(value="`id`",type = IdType.AUTO)
    private int id;

    /**
    * 抽奖记录id
    */
    @ApiModelProperty(value = "抽奖记录id", dataType = "int")
    @TableField("`lottery_log_id`")
    private int lotteryLogId;

    /**
    * 抽奖记录id
    */
    @ApiModelProperty(value = "客户id", dataType = "int")
    @TableField("`customer_id`")
    private int customerId;

    /**
    * 收件人
    */
    @ApiModelProperty(value = "收件人", dataType = "String")
    @TableField("`recipient`")
    private String recipient;

    /**
    * 手机号码
    */
    @ApiModelProperty(value = "手机号码", dataType = "String")
    @TableField("`mobile`")
    private String mobile;

    /**
    * 所在城市id
    */
    @ApiModelProperty(value = "所在城市id", dataType = "long")
    @TableField("`city_id`")
    private long cityId;

    /**
    * 详细地址
    */
    @ApiModelProperty(value = "详细地址", dataType = "String")
    @TableField("`address`")
    private String address;

    /**
    * 快递单号
    */
    @ApiModelProperty(value = "快递单号", dataType = "String")
    @TableField("`delivery_num`")
    private String deliveryNum;

    /**
     * 快递公司
     */
    @ApiModelProperty(value = "快递公司", dataType = "String")
    @TableField("`delivery_name`")
    private String deliveryName;

    /**
    * 发货时间
    */
    @ApiModelProperty(value = "发货时间", dataType = "Date")
    @TableField("`dispatched_time`")
    private Date dispatchedTime;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间", dataType = "Date")
    @TableField("`create_time`")
    private Date createTime;

    /**
    * 通用状态,2正常,3删除
    */
    @ApiModelProperty(value = "通用状态,2正常,3删除", dataType = "int")
    @TableField("`data_status`")
    private int dataStatus;

}
