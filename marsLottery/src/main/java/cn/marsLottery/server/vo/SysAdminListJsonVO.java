package cn.marsLottery.server.vo;

import cn.marsLottery.server.po.SysAdmin;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 表[sys_admin]对应实体类
 *
 * @author auto
 */

@Data
public class SysAdminListJsonVO {

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
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称", dataType = "String")
    private String nickname;

    /**
     * 头像地址
     */
    @ApiModelProperty(value = "头像地址", dataType = "String")
    private String avatar;

    public SysAdminListJsonVO(SysAdmin admin) {
        if (admin != null) {
            BeanUtils.copyProperties(admin, this);
        }
    }

}
