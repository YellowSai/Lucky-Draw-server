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
* 表[prize]对应实体类
*
* @author auto
*
*/

@Data
@TableName(value = "`prize`", schema="mars_lottery")
@ApiModel(value = "表prize的实体类")
public class Prize {

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
    * 奖品名称
    */
    @ApiModelProperty(value = "奖品名称", dataType = "String")
    @TableField("`name`")
    private String name;

    /**
    * 奖品图片
    */
    @ApiModelProperty(value = "奖品图片", dataType = "String")
    @TableField("`image`")
    private String image;

    /**
     * 几等奖
     */
    @ApiModelProperty(value = "几等奖", dataType = "int")
    @TableField("`award`")
    private int award;

    /**
    * 奖品描述
    */
    @ApiModelProperty(value = "奖品描述", dataType = "String")
    @TableField("`describe`")
    private String describe;

    /**
    * 奖品价值
    */
    @ApiModelProperty(value = "奖品价值", dataType = "Double")
    @TableField("`price`")
    private Double price;

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
