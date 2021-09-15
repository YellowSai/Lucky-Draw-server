package cn.marsLottery.server.controller.admapi;

import cn.jzcscw.commons.annotation.OperateLog;
import cn.jzcscw.commons.core.Result;
import cn.marsLottery.server.dto.SysPermissionDTO;
import cn.marsLottery.server.po.SysPermission;
import cn.marsLottery.server.service.SysPermissionService;
import cn.marsLottery.server.vo.SysPermissionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限与角色管理
 *
 * @author tanghuang 2020年02月23日
 */
@Slf4j
@Api(tags = {"权限与角色管理"})
@RestController
@RequestMapping("/admapi/permission")
public class SysPermissionAdminController {

    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 获取权限列表
     *
     * @return List<SysPermissionVO>
     */
    @ApiOperation(value = "获取权限列表")
    @GetMapping("/list")
    public Result<List<SysPermissionVO>> list() {
        List<SysPermission> sysPermissionList = sysPermissionService.listBySortDesc(0);
        List<SysPermissionVO> sysPermissionVOList = sysPermissionList.stream().map(SysPermissionVO::new).collect(Collectors.toList());
        return Result.data(sysPermissionVOList);
    }

    /**
     * 权限树状列表
     *
     * @return List<SysPermissionVO>
     */
    @ApiOperation(value = "权限树状列表")
    @GetMapping("/tree")
    public Result<List<SysPermissionVO>> tree() {
        List<SysPermission> sysPermissionList = sysPermissionService.listBySortDesc(0);
        List<SysPermissionVO> sysPermissionVOList = sysPermissionService.generateTree(sysPermissionList);
        return Result.data(sysPermissionVOList);
    }

    @PostMapping("/add")
    @OperateLog(description = "增加系统权限配置")
    public Result<String> add(@RequestBody @Valid SysPermissionDTO sysPermissionDTO) {
        SysPermission sysPermission = sysPermissionService.getByPath(sysPermissionDTO.getPath());
        if (sysPermission != null) {
            return Result.error("该权限路径已存在");
        }
        sysPermission = sysPermissionDTO.toSysPermission();
        sysPermissionService.save(sysPermission);
        return Result.ok("添加成功");
    }

    @PutMapping("/update")
    @OperateLog(description = "修改系统权限配置")
    public Result<String> update(@RequestBody @Valid SysPermissionDTO sysPermissionDTO) {
        SysPermission sysPermission = sysPermissionService.getById(sysPermissionDTO.getId());
        if (sysPermission == null) {
            return Result.error("权限不存在");
        }
        sysPermissionService.updateById(sysPermissionDTO.toSysPermission());
        return Result.ok("修改成功");
    }

    /**
     * 删除权限
     *
     * @param id id
     * @return result
     */
    @ApiOperation(value = "删除权限")
    @DeleteMapping("/delete/{id}")
    @OperateLog(description = "删除系统权限配置")
    public Result<String> del(@RequestBody @PathVariable int id) {
        SysPermission sysPermission = sysPermissionService.getById(id);
        if (sysPermission == null) {
            return Result.error("该权限不存在");
        }
        List<SysPermission> childrenList = sysPermissionService.listByParentId(id);
        List<Integer> idList = new ArrayList<>();
        if (childrenList != null || childrenList.size() > 0) {
            idList = childrenList.stream().map(SysPermission::getId).collect(Collectors.toList());
        }
        idList.add(id);
        sysPermissionService.removeByIds(idList);
        return Result.ok("删除成功");
    }

    @GetMapping("/{id}")
    public Result<SysPermission> permissionInfo(@PathVariable int id) {
        if (id <= 0) {
            return Result.error("该权限不存在");
        }
        SysPermission permission = sysPermissionService.getById(id);
        if (permission == null) {
            return Result.error("该权限不存在");
        }
        return Result.data(permission);
    }

    @GetMapping("listJson")
    public Result<List<SysPermission>> listJson() {
        List<SysPermission> permissionList = sysPermissionService.list();
        return Result.data(permissionList);
    }
}
