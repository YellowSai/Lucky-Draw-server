package cn.marsLottery.server.vo;

import cn.marsLottery.server.po.SysAdmin;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

@Data
public class SysAdminVO {
    private int id;

    private String username;

    private String nickname;

    private int roleId;

    private String avatar;

    private String mobile;

    private Date loginDate;

    private SysRoleVO role;

    private List<SysPermissionVO> menu;
    private List<SysPermissionVO> permissionList;

    public SysAdminVO(SysAdmin admin) {
        if (admin != null) {
            BeanUtils.copyProperties(admin, this);
        }
    }
}
