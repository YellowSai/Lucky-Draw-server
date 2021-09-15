package cn.marsLottery.server.service.impl;

import cn.marsLottery.server.dao.SysPermissionDao;
import cn.marsLottery.server.po.SysPermission;
import cn.marsLottery.server.service.SysPermissionService;
import cn.marsLottery.server.vo.SysPermissionVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SysPermission对应的服务类实现
 *
 * @author auto
 */

@Service("sysPermissionService")
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionDao, SysPermission> implements SysPermissionService {

    @Override
    public SysPermission getByPath(String path) {
        return this.getOne(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getPath, path));
    }

    @Override
    public List<SysPermissionVO> generateTree(List<SysPermission> list) {
        if (list == null || list.size() == 0) {
            return new ArrayList<>();
        }

        List<SysPermissionVO> tree = new ArrayList<>();
        List<SysPermissionVO> nodes = list.stream().map(SysPermissionVO::new).collect(Collectors.toList());
        for (Iterator<SysPermissionVO> ite = nodes.iterator(); ite.hasNext(); ) {
            SysPermissionVO permission = ite.next();
            if (permission.getParentId() == 0) {
                tree.add(permission);
                ite.remove();
            }
        }
        tree.forEach(c -> setChildren(c, nodes));
        return tree;
    }

    @Override
    public List<SysPermission> listBySortDesc(int parentId) {
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
        if (parentId > 0) {
            query.eq(SysPermission::getParentId, parentId);
        }
        query.orderByDesc(SysPermission::getSort);
        query.orderByAsc(SysPermission::getId);
        return list(query);
    }


    @Override
    public List<SysPermission> getPermissionListByIdIn(List<Long> permissionIdList) {
        if (permissionIdList == null || permissionIdList.size() == 0) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getId, permissionIdList)
                .orderByDesc(SysPermission::getSort)
                .orderByAsc(SysPermission::getId);
        return list(queryWrapper);
    }

    @Override
    public List<SysPermission> listByParentId(int id) {
        LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPermission::getParentId, id);
        return this.list(queryWrapper);
    }

    private void setChildren(SysPermissionVO parent, List<SysPermissionVO> list) {
        List<SysPermissionVO> children = new ArrayList<>();
        for (Iterator<SysPermissionVO> ite = list.iterator(); ite.hasNext(); ) {
            SysPermissionVO city = ite.next();
            if (city.getParentId() == parent.getId()) {
                children.add(city);
                ite.remove();
            }
        }
        if (children.isEmpty()) {
            return;
        }
        parent.setChildren(children);
        children.forEach(c -> setChildren(c, list));
    }
}
