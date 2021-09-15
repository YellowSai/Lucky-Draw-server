package cn.marsLottery.server.dto;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.marsLottery.server.po.LotteryCustomer;

    import java.util.Date;

/**
* 表[lottery_customer]对应AddDTO类
*
* @author auto
*
*/

@Data
public class LotteryCustomerAddDTO {

    /**
    * 自增id
    */
    @ApiModelProperty(value = "自增id", dataType = "int")
    private int id;

    /**
    * 活动id
    */
    @ApiModelProperty(value = "活动id", dataType = "int")
    private int lotteryId;

    /**
    * 客户id
    */
    @ApiModelProperty(value = "客户id", dataType = "int")
    private int customerId;

    /**
    * 抽奖总次数
    */
    @ApiModelProperty(value = "抽奖总次数", dataType = "int")
    private int drawsTimes;

    /**
    * 一等奖
    */
    @ApiModelProperty(value = "一等奖", dataType = "int")
    private int firstPrize;

    /**
    * 二等奖
    */
    @ApiModelProperty(value = "二等奖", dataType = "int")
    private int secondPrize;

    /**
    * 三等奖
    */
    @ApiModelProperty(value = "三等奖", dataType = "int")
    private int thirdPrize;

    /**
    * 四等奖
    */
    @ApiModelProperty(value = "四等奖", dataType = "int")
    private int fourthPrize;

    /**
    * 不中奖
    */
    @ApiModelProperty(value = "不中奖", dataType = "int")
    private int notPrize;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间", dataType = "Date")
    private Date createTime;

    /**
    * 通用状态,2正常,3删除
    */
    @ApiModelProperty(value = "通用状态,2正常,3删除", dataType = "int")
    private int dataStatus;
    public LotteryCustomer toLotteryCustomer() {
        LotteryCustomer lotteryCustomer = new LotteryCustomer();
        BeanUtil.copyProperties(this, lotteryCustomer);
        return lotteryCustomer;
    }
}
