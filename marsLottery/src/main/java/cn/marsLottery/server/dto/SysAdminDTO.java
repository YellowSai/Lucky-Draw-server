package cn.marsLottery.server.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.marsLottery.server.po.SysAdmin;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysAdminDTO {

    @ApiModelProperty(value = "用户ID", dataType = "int")
    private int id;

    @ApiModelProperty(value = "角色ID", dataType = "int")
    private int roleId;

    @ApiModelProperty(value = "用户账号", dataType = "String")
    private String username;

    @ApiModelProperty(value = "用户昵称", dataType = "String")
    private String nickname;

    @ApiModelProperty(value = "密码", dataType = "String")
    private String password;

    @ApiModelProperty(value = "确认密码", dataType = "String")
    private String confirmPassword;

    @ApiModelProperty(value = "头像地址", dataType = "String")
    private String avatar;

    @ApiModelProperty(value = "手机号码", dataType = "String")
    private String mobile;

    @ApiModelProperty(value = "用户邮箱", dataType = "String")
    private String email;

    @ApiModelProperty(value = "备注", dataType = "String")
    private String remark;

    @ApiModelProperty(value = "帐号状态（1正常 2停用）", dataType = "String")
    private String status;

    public SysAdmin toAdmin() {
        SysAdmin admin = new SysAdmin();
        BeanUtil.copyProperties(this, admin);
        return admin;
    }

}
