package cn.marsLottery.server.controller.admapi;

import cn.hutool.core.collection.CollectionUtil;
import cn.jzcscw.commons.annotation.OperateLog;
import cn.jzcscw.commons.constant.DataStatus;
import cn.jzcscw.commons.core.Result;
import cn.jzcscw.commons.util.StringUtil;
import cn.marsLottery.server.enums.UserRoleType;
import cn.marsLottery.server.po.SysRole;
import cn.marsLottery.server.dto.SysRoleDTO;
import cn.marsLottery.server.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admapi/role")
public class SysRoleAdminController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/list")
    public Result<Page<SysRole>> list(@RequestParam(required = false, defaultValue = "") String keywords, Page page) {
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtil.isNotEmpty(keywords)) {
            sysRoleLambdaQueryWrapper.like(SysRole::getRoleName, keywords);
        }
        sysRoleLambdaQueryWrapper.eq(SysRole::getDataStatus, DataStatus.NORMAL.getValue());
        Page<SysRole> pageData = sysRoleService.page(page, sysRoleLambdaQueryWrapper);
        return Result.data(pageData);
    }

    @PutMapping("/update")
    @OperateLog(description = "????????????????????????")
    public Result<String> update(@RequestBody SysRoleDTO sysRoleDTO) {
        SysRole role = sysRoleService.getById(sysRoleDTO.getId());
        if (role == null) {
            return Result.error("???????????????");
        }
        sysRoleService.updateById(sysRoleDTO.toSysRole());
        return Result.ok("????????????");
    }

    @PostMapping("/add")
    @OperateLog(description = "??????????????????")
    public Result<String> add(@RequestBody SysRoleDTO sysRoleDTO) {
        SysRole role = sysRoleService.getByRoleName(sysRoleDTO.getRoleName());
        if (role != null) {
            return Result.error("???????????????");
        }
        sysRoleService.save(sysRoleDTO.toSysRole());
        return Result.ok("????????????");
    }

    @DeleteMapping("/delete/{id}")
    @OperateLog(description = "??????????????????")
    public Result<String> delete(@PathVariable int id) {
        if (id <= UserRoleType.BRAND.getValue()) {
            return Result.error("????????????????????????/???????????????");
        }
        SysRole role = sysRoleService.getById(id);
        if (role == null) {
            return Result.error("???????????????");
        }
        sysRoleService.removeById(id);
        return Result.ok("????????????");
    }

    @GetMapping("/listJson")
    public Result<List<SysRole>> listJson() {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getDataStatus, DataStatus.NORMAL.getValue());

        List<Integer> idNotIn = Arrays.asList(3);
        if (CollectionUtil.isNotEmpty(idNotIn)) {
            queryWrapper.notIn(SysRole::getId, idNotIn);
        }

        List<SysRole> roleList = sysRoleService.list(queryWrapper);
        return Result.data(roleList);
    }

    @GetMapping("/{id}")
    public Result<SysRole> roleInfo(@PathVariable int id) {
        if (id <= 0) {
            return Result.error("???????????????");
        }
        SysRole role = sysRoleService.getById(id);
        if (role == null) {
            return Result.error("???????????????");
        }
        return Result.data(role);
    }
}
