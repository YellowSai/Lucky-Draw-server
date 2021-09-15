package cn.marsLottery.server.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.marsLottery.server.po.Customer;
import cn.marsLottery.server.po.Lottery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.marsLottery.server.po.LotteryCustomer;

    import java.util.Date;
import lombok.Data;

/**
* 表[lottery_customer]对应VO类
*
* @author auto
*
*/

@Data
public class LotteryCustomerVO {

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
     * 客户信息
     */
    @ApiModelProperty(value = "客户信息", dataType = "Customer")
    private Customer customer;

    /**
     * 活动信息
     */
    @ApiModelProperty(value = "活动信息", dataType = "Lottery")
    private Lottery lottery;

    /**
    * 通用状态,2正常,3删除
    */
    @ApiModelProperty(value = "通用状态,2正常,3删除", dataType = "int")
    private int dataStatus;

    public LotteryCustomerVO(LotteryCustomer lotteryCustomer) {
        if (lotteryCustomer != null) {
            BeanUtil.copyProperties(lotteryCustomer, this);
        }
    }
}
