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
* 表[sys_role]对应实体类
*
* @author auto
*
*/

@Data
@TableName(value = "`sys_role`")
@ApiModel(value = "表sys_role的实体类")
public class SysRole {

    /**
    * 自增id
    */
    @ApiModelProperty(value = "自增id", dataType = "int")
    @TableField("`id`")
    @TableId(value="`id`",type = IdType.AUTO)
    private int id;

    /**
    * 角色名称
    */
    @ApiModelProperty(value = "角色名称", dataType = "String")
    @TableField("`role_name`")
    private String roleName;

    /**
    * 特殊角色标识
    */
    @ApiModelProperty(value = "特殊角色标识", dataType = "String")
    @TableField("`key`")
    private String key;

    /**
    * 备注
    */
    @ApiModelProperty(value = "备注", dataType = "String")
    @TableField("`remark`")
    private String remark;

    /**
    * 权限id，多个以逗号分隔
    */
    @ApiModelProperty(value = "权限id，多个以逗号分隔", dataType = "String")
    @TableField("`permissions`")
    private String permissions;

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
