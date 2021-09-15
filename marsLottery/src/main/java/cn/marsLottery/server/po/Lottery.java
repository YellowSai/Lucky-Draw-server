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
* 表[lottery]对应实体类
*
* @author auto
*
*/

@Data
@TableName(value = "`lottery`", schema="mars_lottery")
@ApiModel(value = "表lottery的实体类")
public class Lottery {

    /**
    * 自增id
    */
    @ApiModelProperty(value = "自增id", dataType = "int")
    @TableField("`id`")
    @TableId(value="`id`",type = IdType.AUTO)
    private int id;

    /**
    * 活动名称
    */
    @ApiModelProperty(value = "活动名称", dataType = "String")
    @TableField("`name`")
    private String name;

    /**
    * 描述
    */
    @ApiModelProperty(value = "描述", dataType = "String")
    @TableField("`description`")
    private String description;

    /**
     * 进货时间
     */
    @ApiModelProperty(value = "进货时间", dataType = "String")
    @TableField("`purchase_time`")
    private String purchaseTime;

    /**
    * 开始时间
    */
    @ApiModelProperty(value = "开始时间", dataType = "Date")
    @TableField("`start_time`")
    private Date startTime;

    /**
    * 结束时间
    */
    @ApiModelProperty(value = "结束时间", dataType = "Date")
    @TableField("`end_time`")
    private Date endTime;

    /**
    * 活动是否开启
    */
    @ApiModelProperty(value = "活动是否开启", dataType = "String")
    @TableField("`is_start`")
    private String isStart;

    /**
    * 活动说明
    */
    @ApiModelProperty(value = "活动说明", dataType = "String")
    @TableField("`explain`")
    private String explain;

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
