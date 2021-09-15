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
* 表[lottery_log]对应实体类
*
* @author auto
*
*/

@Data
@TableName(value = "`lottery_log`", schema="mars_lottery")
@ApiModel(value = "表lottery_log的实体类")
public class LotteryLog {

    /**
    * 
    */
    @ApiModelProperty(value = "", dataType = "int")
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
    * 抽奖奖品表id
    */
    @ApiModelProperty(value = "抽奖奖品表id", dataType = "int")
    @TableField("`prize_id`")
    private int prizeId;

    /**
    * 客户id
    */
    @ApiModelProperty(value = "客户id", dataType = "int")
    @TableField("`customer_id`")
    private int customerId;

    /**
    * 是否中奖
    */
    @ApiModelProperty(value = "是否中奖", dataType = "String")
    @TableField("`is_winner`")
    private String isWinner;

    /**
    * 是否已发货
    */
    @ApiModelProperty(value = "是否已发货", dataType = "String")
    @TableField("`is_dispatched`")
    private String isDispatched;

    /**
     * 是否填写信息
     */
    @ApiModelProperty(value = "是否填写信息", dataType = "String")
    @TableField("`is_fill`")
    private String isFill;

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
