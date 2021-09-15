package cn.marsLottery.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* 表[sys_permission]对应实体类
*
* @author auto
*
*/

@Data
@TableName(value = "`sys_permission`")
@ApiModel(value = "表sys_permission的实体类")
public class SysPermission {

    /**
    * 自增id
    */
    @ApiModelProperty(value = "自增id", dataType = "int")
    @TableField("`id`")
    @TableId(value="`id`",type = IdType.AUTO)
    private int id;

    /**
    * 父权限ID，一级菜单为0
    */
    @ApiModelProperty(value = "父权限ID，一级菜单为0", dataType = "int")
    @TableField("`parent_id`")
    private int parentId;

    /**
    * 类别，1目录，2菜单，3权限
    */
    @ApiModelProperty(value = "类别，1目录，2菜单，3权限", dataType = "int")
    @TableField("`type`")
    private int type;

    /**
    * 权限/菜单名称
    */
    @ApiModelProperty(value = "权限/菜单名称", dataType = "String")
    @TableField("`title`")
    private String title;

    /**
    * 组件名
    */
    @ApiModelProperty(value = "组件名", dataType = "String")
    @TableField("`component`")
    private String component;

    /**
    * 标识名
    */
    @ApiModelProperty(value = "标识名", dataType = "String")
    @TableField("`key`")
    private String key;

    /**
    * 访问路径
    */
    @ApiModelProperty(value = "访问路径", dataType = "String")
    @TableField("`path`")
    private String path;

    /**
    * 默认跳转
    */
    @ApiModelProperty(value = "默认跳转", dataType = "String")
    @TableField("`redirect`")
    private String redirect;

    /**
    * 权限图标，菜单用
    */
    @ApiModelProperty(value = "权限图标，菜单用", dataType = "String")
    @TableField("`icon`")
    private String icon;

    /**
    * 排序权重
    */
    @ApiModelProperty(value = "排序权重", dataType = "int")
    @TableField("`sort`")
    private int sort;

    /**
    * 授权(多个用逗号分隔，如：user:list,user:create)
    */
    @ApiModelProperty(value = "授权(多个用逗号分隔，如：user:list,user:create)", dataType = "String")
    @TableField("`permits`")
    private String permits;

    /**
    * 是否隐藏,Y隐藏，N显示
    */
    @ApiModelProperty(value = "是否隐藏,Y隐藏，N显示", dataType = "String")
    @TableField("`hidden`")
    private String hidden;

    /**
    * 通用状态,2正常,3删除
    */
    @ApiModelProperty(value = "通用状态,2正常,3删除", dataType = "int")
    @TableField("`data_status`")
    private int dataStatus;

}
