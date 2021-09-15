package cn.marsLottery.server.dto;

import cn.marsLottery.server.po.SysPermission;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;

@Data
public class SysPermissionDTO {
    @ApiModelProperty(value = "权限id", dataType = "long")
    private int id;

    @ApiModelProperty(value = "父级权限id，第一级为0", dataType = "long", required = true)
    private int parentId;

    @ApiModelProperty(value = "类别，1目录，2菜单，3权限", dataType = "int", required = true)
    private int type;

    @ApiModelProperty(value = "权限/菜单名称", dataType = "String", required = true)
    @NotEmpty(message = "权限名称不能为空")
    private String title;

    @ApiModelProperty(value = "组件名", dataType = "String")
    private String component;

    @ApiModelProperty(value = "标识名", dataType = "String")
    private String key;

    @ApiModelProperty(value = "访问路径", dataType = "String", required = true)
    private String path;

    @ApiModelProperty(value = "默认跳转", dataType = "String")
    private String redirect;

    @ApiModelProperty(value = "图标", dataType = "String")
    private String icon;

    @ApiModelProperty(value = "权重排序", dataType = "int", required = true)
    private int sort;

    @ApiModelProperty(value = "授权", dataType = "String")
    private String permits;

    @ApiModelProperty(value = "是否隐藏", dataType = "String")
    private String hidden;

    @ApiModelProperty(value = "通用状态，2正常，3删除", dataType = "String")
    private int dataStatus;

    public SysPermission toSysPermission() {
        SysPermission sysPermission = new SysPermission();
        BeanUtils.copyProperties(this, sysPermission);
        if (sysPermission.getType() == 1) {
            sysPermission.setParentId(0);
        }
        return sysPermission;
    }
}
