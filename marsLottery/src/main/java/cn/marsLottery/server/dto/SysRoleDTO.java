package cn.marsLottery.server.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.jzcscw.commons.constant.DataStatus;
import cn.marsLottery.server.po.SysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysRoleDTO {
    @ApiModelProperty(value = "角色id", dataType = "String")
    private long id;

    @ApiModelProperty(value = "角色名称", dataType = "String", required = true)
    private String roleName;

    @ApiModelProperty(value = "备注", dataType = "String")
    private String remark;

    @ApiModelProperty(value = "权限的ids", dataType = "String")
    private String permissions;

    @ApiModelProperty(value = "key", dataType = "String")
    private String key;

    public SysRole toSysRole(){
        SysRole sysRole = new SysRole();
        BeanUtil.copyProperties(this,sysRole);
        sysRole.setDataStatus(DataStatus.NORMAL.getValue());
        return sysRole;
    }
}
