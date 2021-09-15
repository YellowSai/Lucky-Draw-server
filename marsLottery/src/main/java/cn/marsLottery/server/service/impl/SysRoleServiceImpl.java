package cn.marsLottery.server.service.impl;

import cn.jzcscw.commons.constant.DataStatus;
import cn.marsLottery.server.dao.SysRoleDao;
import cn.marsLottery.server.po.SysAdmin;
import cn.marsLottery.server.po.SysRole;
import cn.marsLottery.server.service.SysRoleService;
import cn.marsLottery.server.service.SysAdminService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * SysRole对应的服务类实现
 *
 * @author auto
 */

@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRole> implements SysRoleService {

    @Autowired
    private SysAdminService sysAdminService;

    @Override
    public SysRole getByRoleName(String roleName) {
        return this.getOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleName, roleName));
    }

    @Override
    public SysRole get(int adminId) {
        SysAdmin user = sysAdminService.getById(adminId);
        if (user == null) {
            return null;
        }
        int roleId = user.getRoleId();
        LambdaQueryWrapper<SysRole> roleQueryWrapper = new LambdaQueryWrapper<>();
        roleQueryWrapper.eq(SysRole::getId, roleId);
        SysRole role = this.getOne(roleQueryWrapper);
        return role;
    }

    @Override
    public SysRole getByKey(String key) {
        return this.getOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getKey, key));
    }

    @Override
    public List<SysRole> listByIdIn(List<Integer> roleIdList) {
        if (roleIdList == null || roleIdList.size() == 0) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRole::getId, roleIdList).eq(SysRole::getDataStatus, DataStatus.NORMAL.getValue());
        return list(wrapper);
    }

}
