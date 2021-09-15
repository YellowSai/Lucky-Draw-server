package cn.marsLottery.server.dto;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.marsLottery.server.po.LotteryLog;

    import java.util.Date;
/**
* 表[lottery_log]对应UpdateDTO类
*
* @author auto
*
*/

@Data
public class LotteryLogUpdateDTO {

    /**
    * 
    */
    @ApiModelProperty(value = "", dataType = "int")
    private int id;

    /**
    * 活动id
    */
    @ApiModelProperty(value = "活动id", dataType = "int")
    private int lotteryId;

    /**
    * 抽奖奖品表id
    */
    @ApiModelProperty(value = "抽奖奖品表id", dataType = "int")
    private int prizeId;

    /**
    * 客户id
    */
    @ApiModelProperty(value = "客户id", dataType = "int")
    private int customerId;

    /**
    * 是否中奖
    */
    @ApiModelProperty(value = "是否中奖", dataType = "String")
    private String isWinner;

    /**
    * 是否已发货
    */
    @ApiModelProperty(value = "是否已发货", dataType = "String")
    private String isDispatched;

    /**
     * 是否填写信息
     */
    @ApiModelProperty(value = "是否填写信息", dataType = "String")
    private String isFill;

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
    public LotteryLog toLotteryLog() {
        LotteryLog lotteryLog = new LotteryLog();
        BeanUtil.copyProperties(this, lotteryLog);
        return lotteryLog;
    }
}
