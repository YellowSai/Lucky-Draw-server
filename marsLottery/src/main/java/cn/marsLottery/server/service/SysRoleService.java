package cn.marsLottery.server.service;

import cn.marsLottery.server.po.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 表[sys_role]对应的服务类
 *
 * @author auto
 */

public interface SysRoleService extends IService<SysRole> {
    SysRole getByRoleName(String roleName);

    SysRole get(int adminId);

    SysRole getByKey(String key);

    List<SysRole> listByIdIn(List<Integer> roleIdList);
}
