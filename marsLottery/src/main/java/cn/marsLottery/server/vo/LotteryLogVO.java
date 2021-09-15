package cn.marsLottery.server.vo;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.marsLottery.server.po.LotteryLog;

    import java.util.Date;
import lombok.Data;

/**
* 表[lottery_log]对应VO类
*
* @author auto
*
*/

@Data
public class LotteryLogVO {

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

    /**
     * 奖品信息
     */
    @ApiModelProperty(value = "奖品信息", dataType = "Prize")
    private PrizeVO prize;

    /**
     * 客户信息
     */
    @ApiModelProperty(value = "客户信息", dataType = "CustomerVO")
    private CustomerVO customerVO;

    public LotteryLogVO(LotteryLog lotteryLog) {
        if (lotteryLog != null) {
            BeanUtil.copyProperties(lotteryLog, this);
        }
    }
}
