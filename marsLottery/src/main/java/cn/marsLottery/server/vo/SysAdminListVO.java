package cn.marsLottery.server.vo;

import cn.marsLottery.server.po.SysAdmin;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * 表[sys_admin]对应实体类
 *
 * @author auto
 */

@Data
public class SysAdminListVO {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", dataType = "int")
    private int id;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", dataType = "int")
    private int roleId;

    /**
     * 角色类型
     */
    @ApiModelProperty(value = "角色类型", dataType = "int")
    private String role;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号", dataType = "String")
    private String username;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称", dataType = "String")
    private String nickname;

    /**
     * 头像地址
     */
    @ApiModelProperty(value = "头像地址", dataType = "String")
    private String avatar;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码", dataType = "String")
    private String mobile;

    /**
     * 用户邮箱
     */
    @ApiModelProperty(value = "用户邮箱", dataType = "String")
    private String email;

    /**
     * 帐号状态（1正常 2停用）
     */
    @ApiModelProperty(value = "帐号状态（1正常 2停用）", dataType = "String")
    private String status;

    /**
     * 最后登陆IP
     */
    @ApiModelProperty(value = "最后登陆IP", dataType = "String")
    private String loginIp;

    /**
     * 最后登陆时间
     */
    @ApiModelProperty(value = "最后登陆时间", dataType = "Date")
    private Date loginDate;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", dataType = "Date")
    private Date createTime;

    public SysAdminListVO(SysAdmin admin) {
        if (admin != null) {
            BeanUtils.copyProperties(admin, this);
        }
    }

}
