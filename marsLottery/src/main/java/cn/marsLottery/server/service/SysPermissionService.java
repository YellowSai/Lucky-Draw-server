package cn.marsLottery.server.service;

import cn.marsLottery.server.po.SysPermission;
import cn.marsLottery.server.vo.SysPermissionVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 表[sys_permission]对应的服务类
 *
 * @author auto
 */

public interface SysPermissionService extends IService<SysPermission> {

    SysPermission getByPath(String path);

    List<SysPermissionVO> generateTree(List<SysPermission> list);

    List<SysPermission> listBySortDesc(int parentId);

    List<SysPermission> getPermissionListByIdIn(List<Long> permisionIdList);

    List<SysPermission> listByParentId(int id);

}
