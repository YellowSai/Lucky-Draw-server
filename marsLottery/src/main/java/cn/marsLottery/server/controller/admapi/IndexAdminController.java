package cn.marsLottery.server.controller.admapi;

import cn.hutool.crypto.SecureUtil;
import cn.jzcscw.commons.annotation.ApiAuth;
import cn.jzcscw.commons.annotation.OperateLog;
import cn.jzcscw.commons.constant.AdminStatus;
import cn.jzcscw.commons.constant.Consts;
import cn.jzcscw.commons.core.Result;
import cn.jzcscw.commons.web.RequestUtil;
import cn.marsLottery.server.po.SysAdmin;
import cn.marsLottery.server.po.SysPermission;
import cn.marsLottery.server.po.SysRole;
import cn.marsLottery.server.service.SysAdminService;
import cn.marsLottery.server.dto.AdminLoginDTO;
import cn.marsLottery.server.service.SysPermissionService;
import cn.marsLottery.server.service.SysRoleService;
import cn.marsLottery.server.vo.AdminLoginResult;
import cn.marsLottery.server.vo.SysAdminVO;
import cn.marsLottery.server.vo.SysRoleVO;
import cn.marsLottery.server.web.AdminWebContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/admapi")
@ApiAuth(ignore = true)
@Api
public class IndexAdminController {

    @Autowired
    private SysAdminService sysAdminService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysPermissionService sysPermissionService;


    @ApiOperation(value = "管理员登录", notes = "登陆成功则携带token返回")
    @PostMapping("/login")
    @OperateLog(description = "登录系统")
    public Result<AdminLoginResult> login(@RequestBody @Validated AdminLoginDTO loginDTO, @ApiIgnore HttpServletRequest request) {
        log.debug("loginDTO:{}", loginDTO);
        SysAdmin sysAdmin = sysAdminService.getByUserName(loginDTO.getUsername());
        if (sysAdmin == null) {
            return Result.error(Consts.FAILED_CODE, "账号不存在");
        }
        if (!sysAdmin.getPassword().equals(SecureUtil.md5(loginDTO.getPassword()))) {
            return Result.error(Consts.FAILED_CODE, "密码错误");
        }
        if (sysAdmin.getRoleId() <= 0) {
            return Result.error("你还没有分配后台角色，无法登录系统");
        }
        if (AdminStatus.isDisable(sysAdmin.getStatus())) {
            return Result.error(Consts.FAILED_CODE, "账号已被禁用");
        }
        SysRole role = sysRoleService.getById(sysAdmin.getRoleId());
        if (role == null) {
            return Result.error("您的角色错误，无法登录系统");
        }

        //生成jwt
        String token = sysAdminService.createJWT(sysAdmin);
        //token存入缓存中
        String ip = RequestUtil.getRemoteAddr(request);
        sysAdminService.login(sysAdmin.getId(), token, ip);

        SysRoleVO roleVO = new SysRoleVO(role);
        List<Long> permissionIdList = Arrays.stream(role.getPermissions().split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<SysPermission> permissionList = sysPermissionService.getPermissionListByIdIn(permissionIdList);

        SysAdminVO sysAdminVO = new SysAdminVO(sysAdmin);
        sysAdminVO.setPermissionList(sysPermissionService.generateTree(permissionList));
        sysAdminVO.setRole(roleVO);

        AdminLoginResult adminLoginResult = new AdminLoginResult();
        adminLoginResult.setToken(token);
        adminLoginResult.setAdmin(sysAdminVO);

        return Result.data(adminLoginResult);
    }

    @ApiOperation(value = "管理员退出登录")
    @PostMapping("/logout")
    @OperateLog(description = "退出系统")
    public Result<String> logout() {
        String token = AdminWebContext.getToken();
        int adminId = AdminWebContext.getAdminId();
        sysAdminService.logout(adminId, token);
        return Result.ok();
    }
}
