package cn.marsLottery.server.vo;

import cn.marsLottery.server.po.SysRole;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class SysRoleVO {

    private Long id;

    private String roleName;

    private String key;

    public SysRoleVO(SysRole role) {
        if (role != null) {
            BeanUtils.copyProperties(role, this);
        }
    }
}
