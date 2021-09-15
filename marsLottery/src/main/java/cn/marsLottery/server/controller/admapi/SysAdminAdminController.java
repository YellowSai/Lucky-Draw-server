package cn.marsLottery.server.controller.admapi;

import cn.hutool.crypto.SecureUtil;
import cn.jzcscw.commons.annotation.OperateLog;
import cn.jzcscw.commons.constant.AdminStatus;
import cn.jzcscw.commons.core.Result;
import cn.jzcscw.commons.util.StringUtil;
import cn.marsLottery.server.dto.SysAdminDTO;
import cn.marsLottery.server.dto.UpdateInfoDTO;
import cn.marsLottery.server.po.SysAdmin;
import cn.marsLottery.server.po.SysPermission;
import cn.marsLottery.server.po.SysRole;
import cn.marsLottery.server.service.SysAdminService;
import cn.marsLottery.server.service.SysPermissionService;
import cn.marsLottery.server.service.SysRoleService;
import cn.marsLottery.server.vo.SysAdminListJsonVO;
import cn.marsLottery.server.vo.SysAdminListVO;
import cn.marsLottery.server.vo.SysAdminVO;
import cn.marsLottery.server.vo.SysRoleVO;
import cn.marsLottery.server.web.AdminWebContext;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admapi/admin")
public class SysAdminAdminController {

    @Autowired
    private SysAdminService sysAdminService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @GetMapping("/info")
    public Result<SysAdminVO> info() {
        int adminId = AdminWebContext.getAdminId();

        SysAdmin sysAdmin = sysAdminService.getById(adminId);
        List<SysPermission> permissionList = new ArrayList<>();
        SysRoleVO roleVO = null;
        if (sysAdmin.getRoleId() > 0) {
            SysRole role = sysRoleService.getById(sysAdmin.getRoleId());
            if (role != null) {
                roleVO = new SysRoleVO(role);
                List<Long> permissionIdList = Arrays.stream(role.getPermissions().split(",")).map(Long::parseLong).collect(Collectors.toList());
                permissionList = sysPermissionService.getPermissionListByIdIn(permissionIdList);
            }
        }
        SysAdminVO sysAdminVO = new SysAdminVO(sysAdmin);
        sysAdminVO.setPermissionList(sysPermissionService.generateTree(permissionList));
        sysAdminVO.setRole(roleVO);
        return Result.data(sysAdminVO);
    }

    @GetMapping("/list")
    public Result<Page<SysAdminListVO>> list(@RequestParam(required = false, defaultValue = "") String keywords,
                                             @RequestParam(required = false, defaultValue = "0") int status,
                                             Page page) {
        QueryWrapper<SysAdmin> userQueryWrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(keywords)) {
            userQueryWrapper.like("username", keywords);
        }

        if (status != 0) {
            userQueryWrapper.eq("status", status);
        }
        userQueryWrapper.orderByAsc("id");
        Page<SysAdmin> pageData = sysAdminService.page(page, userQueryWrapper);
        List<SysAdminListVO> sysAdminVOList = pageData.getRecords().stream().map(SysAdminListVO::new).collect(Collectors.toList());
        Page<SysAdminListVO> result = new Page<>();
        result.setTotal(pageData.getTotal());
        result.setRecords(sysAdminVOList);
        return Result.data(result);
    }

    @GetMapping("/listJson")
    public Result<List<SysAdminListJsonVO>> listJson(@RequestParam(required = false, defaultValue = "") String keywords,
                                                     @RequestParam(required = false, defaultValue = "worker") String roleKey) {
        QueryWrapper<SysAdmin> userQueryWrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(keywords)) {
            userQueryWrapper.like("username", keywords);
        }
        if (StringUtil.isNotEmpty(roleKey)) {
            SysRole sysRole = sysRoleService.getOne(new QueryWrapper<SysRole>().eq("`key`", roleKey));
            if (sysRole != null) {
                userQueryWrapper.eq("role_id", sysRole.getId());
            }
        }
        List<SysAdmin> sysAdminList = sysAdminService.list(userQueryWrapper);
        List<SysAdminListJsonVO> sysAdminVOList = sysAdminList.stream().map(SysAdminListJsonVO::new).collect(Collectors.toList());
        return Result.data(sysAdminVOList);
    }

    @GetMapping("/{id}")
    public Result<SysAdmin> userInfo(@PathVariable int id) {
        if (id <= 0) {
            return Result.error("用户不存在");
        }
        SysAdmin sysAdmin = sysAdminService.getById(id);
        return Result.data(sysAdmin);
    }

    @PutMapping("/update")
    public Result<String> update(@Validated @RequestBody SysAdminDTO sysAdminDTO) {
        SysAdmin sysAdmin = sysAdminService.getById(sysAdminDTO.getId());
        if (sysAdmin == null) {
            return Result.error("用户不存在");
        }
        sysAdmin.setNickname(sysAdminDTO.getNickname());
        sysAdmin.setAvatar(sysAdminDTO.getAvatar());
        sysAdmin.setEmail(sysAdminDTO.getEmail());
        sysAdmin.setMobile(sysAdminDTO.getMobile());
        sysAdmin.setRemark(sysAdminDTO.getRemark());
        sysAdmin.setRoleId(sysAdminDTO.getRoleId());
        sysAdminService.updateById(sysAdmin);
        return Result.ok("修改成功");
    }

    @PostMapping("/updateInfo")
    @OperateLog(description = "更改管理员账号基本信息")
    public Result<String> updateInfo(@Validated @RequestBody UpdateInfoDTO updateInfoDTO) {
        SysAdmin sysAdmin = sysAdminService.getById(updateInfoDTO.getId());
        if (sysAdmin == null) {
            return Result.error("用户不存在");
        }
        sysAdmin.setNickname(updateInfoDTO.getNickname());
        sysAdmin.setAvatar(updateInfoDTO.getAvatar());
        if (sysAdmin.getRoleId() != 0) {
            sysAdmin.setRoleId(updateInfoDTO.getRoleId());
        }
        sysAdminService.updateById(sysAdmin);
        return Result.ok("修改成功");
    }

    @PostMapping("/disable/{id}")
    public Result<String> disable(@PathVariable int id) {
        SysAdmin sysAdmin = sysAdminService.getById(id);
        if (sysAdmin == null) {
            return Result.error("用户不存在");
        }
        sysAdmin.setStatus(AdminStatus.isNormal(sysAdmin.getStatus()) ? AdminStatus.DISABLE.getValue() : AdminStatus.NORMAL.getValue());
        sysAdminService.updateById(sysAdmin);
        String result = AdminStatus.isNormal(sysAdmin.getStatus()) ? "启用成功" : "禁用成功";
        return Result.ok(result);
    }

    @PutMapping("/updatePassword")
    public Result<String> updatePassword(@Validated @RequestBody SysAdminDTO sysAdminDTO) {
        SysAdmin sysAdmin = sysAdminService.getById(sysAdminDTO.getId());
        if (sysAdmin == null) {
            return Result.error("用户不存在");
        }
        String password = sysAdminDTO.getPassword();
        sysAdmin.setPassword(SecureUtil.md5(password));
        sysAdminService.updateById(sysAdmin);
        return Result.ok("修改成功");
    }

    @PostMapping("/add")
    public Result<String> add(@Validated @RequestBody SysAdminDTO sysAdminDTO) {
        SysAdmin sysAdmin = sysAdminService.getByMobileAndUsername(sysAdminDTO.getMobile(), sysAdminDTO.getUsername());
        if (sysAdmin != null) {
            if (sysAdmin.getUsername().equals(sysAdminDTO.getUsername())) {
                return Result.error("该账号已注册");
            }
            return Result.error("该手机号已注册");
        }
        if (!sysAdminDTO.getPassword().equals(sysAdminDTO.getConfirmPassword())) {
            return Result.error("两次密码输入不一致，请确认密码");
        }
        sysAdmin = sysAdminDTO.toAdmin();

        String password = sysAdmin.getPassword();
        sysAdmin.setPassword(SecureUtil.md5(password));
        sysAdminService.save(sysAdmin);
        return Result.ok("添加成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable int id) {
        SysAdmin sysAdmin = sysAdminService.getById(id);
        if (sysAdmin == null) {
            Result.error("用户不存在");
        }
        sysAdminService.removeById(id);
        return Result.ok("删除成功");
    }
}
