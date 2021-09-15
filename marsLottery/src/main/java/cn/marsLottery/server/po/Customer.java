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
* 表[customer]对应实体类
*
* @author auto
*
*/

@Data
@TableName(value = "`customer`", schema="mars_lottery")
@ApiModel(value = "表customer的实体类")
public class Customer {

    /**
    * 自增id
    */
    @ApiModelProperty(value = "自增id", dataType = "int")
    @TableField("`id`")
    @TableId(value="`id`",type = IdType.AUTO)
    private int id;

    /**
    * 客户编号
    */
    @ApiModelProperty(value = "客户编号", dataType = "String")
    @TableField("`number`")
    private String number;

    /**
    * 客户名称
    */
    @ApiModelProperty(value = "客户名称", dataType = "String")
    @TableField("`name`")
    private String name;

    /**
    * 负责人名称
    */
    @ApiModelProperty(value = "负责人名称", dataType = "String")
    @TableField("`director_name`")
    private String directorName;

    /**
    * 手机号码
    */
    @ApiModelProperty(value = "手机号码", dataType = "String")
    @TableField("`mobile`")
    private String mobile;

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
