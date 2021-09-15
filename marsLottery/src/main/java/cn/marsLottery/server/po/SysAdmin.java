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
* 表[sys_admin]对应实体类
*
* @author auto
*
*/

@Data
@TableName(value = "`sys_admin`")
@ApiModel(value = "表sys_admin的实体类")
public class SysAdmin {

    /**
    * 用户ID
    */
    @ApiModelProperty(value = "用户ID", dataType = "int")
    @TableField("`id`")
    @TableId(value="`id`",type = IdType.AUTO)
    private int id;

    /**
    * 角色ID
    */
    @ApiModelProperty(value = "角色ID", dataType = "int")
    @TableField("`role_id`")
    private int roleId;

    /**
    * 用户账号
    */
    @ApiModelProperty(value = "用户账号", dataType = "String")
    @TableField("`username`")
    private String username;

    /**
    * 用户昵称
    */
    @ApiModelProperty(value = "用户昵称", dataType = "String")
    @TableField("`nickname`")
    private String nickname;

    /**
    * 密码
    */
    @ApiModelProperty(value = "密码", dataType = "String")
    @TableField("`password`")
    private String password;

    /**
    * 头像地址
    */
    @ApiModelProperty(value = "头像地址", dataType = "String")
    @TableField("`avatar`")
    private String avatar;

    /**
    * 手机号码
    */
    @ApiModelProperty(value = "手机号码", dataType = "String")
    @TableField("`mobile`")
    private String mobile;

    /**
    * 用户邮箱
    */
    @ApiModelProperty(value = "用户邮箱", dataType = "String")
    @TableField("`email`")
    private String email;

    /**
    * 备注
    */
    @ApiModelProperty(value = "备注", dataType = "String")
    @TableField("`remark`")
    private String remark;

    /**
    * 帐号状态（1正常 2停用）
    */
    @ApiModelProperty(value = "帐号状态（1正常 2停用）", dataType = "String")
    @TableField("`status`")
    private String status;

    /**
    * 最后登陆IP
    */
    @ApiModelProperty(value = "最后登陆IP", dataType = "String")
    @TableField("`login_ip`")
    private String loginIp;

    /**
    * 最后登陆时间
    */
    @ApiModelProperty(value = "最后登陆时间", dataType = "Date")
    @TableField("`login_date`")
    private Date loginDate;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间", dataType = "Date")
    @TableField("`create_time`")
    private Date createTime;

}
