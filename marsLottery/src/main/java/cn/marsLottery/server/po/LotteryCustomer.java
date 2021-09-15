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
* 表[lottery_customer]对应实体类
*
* @author auto
*
*/

@Data
@TableName(value = "`lottery_customer`", schema="mars_lottery")
@ApiModel(value = "表lottery_customer的实体类")
public class LotteryCustomer {

    /**
    * 自增id
    */
    @ApiModelProperty(value = "自增id", dataType = "int")
    @TableField("`id`")
    @TableId(value="`id`",type = IdType.AUTO)
    private int id;

    /**
    * 活动id
    */
    @ApiModelProperty(value = "活动id", dataType = "int")
    @TableField("`lottery_id`")
    private int lotteryId;

    /**
    * 客户id
    */
    @ApiModelProperty(value = "客户id", dataType = "int")
    @TableField("`customer_id`")
    private int customerId;

    /**
    * 抽奖总次数
    */
    @ApiModelProperty(value = "抽奖总次数", dataType = "int")
    @TableField("`draws_times`")
    private int drawsTimes;

    /**
    * 一等奖
    */
    @ApiModelProperty(value = "一等奖", dataType = "int")
    @TableField("`first_prize`")
    private int firstPrize;

    /**
    * 二等奖
    */
    @ApiModelProperty(value = "二等奖", dataType = "int")
    @TableField("`second_prize`")
    private int secondPrize;

    /**
    * 三等奖
    */
    @ApiModelProperty(value = "三等奖", dataType = "int")
    @TableField("`third_prize`")
    private int thirdPrize;

    /**
    * 四等奖
    */
    @ApiModelProperty(value = "四等奖", dataType = "int")
    @TableField("`fourth_prize`")
    private int fourthPrize;

    /**
    * 不中奖
    */
    @ApiModelProperty(value = "不中奖", dataType = "int")
    @TableField("`not_prize`")
    private int notPrize;

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
