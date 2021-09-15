package cn.marsLottery.server.controller.admapi;

import cn.jzcscw.commons.core.Result;
import cn.jzcscw.commons.util.StringUtil;
import cn.marsLottery.server.po.SysLog;
import cn.marsLottery.server.service.SysLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 表[sys_log]对应的后台管理类
 *
 * @author auto
 */

@Slf4j
@RestController
@RequestMapping("/admapi/sysLog")
@Api(tags = {"操作日志管理接口"})
public class SysLogAdminController {

    @Autowired
    private SysLogService sysLogService;

    @ApiOperation(value = "操作日志-列表")
    @GetMapping("/list")
    public Result<Page<SysLog>> list(Page page, @RequestParam(required = false, defaultValue = "") String keywords) {
        LambdaQueryWrapper<SysLog> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtil.isNotEmpty(keywords)) {
            queryWrapper.like(SysLog::getUserName, keywords);
        }
        queryWrapper.orderByDesc(SysLog::getId);
        Page<SysLog> pageData = sysLogService.page(page, queryWrapper);
        return Result.data(pageData);
    }

    @ApiOperation(value = "操作日志-详情")
    @GetMapping("/detail")
    public Result<SysLog> detail(int id) {
        if (id <= 0) {
            return Result.error("操作日志不存在");
        }
        SysLog sysLog = sysLogService.getById(id);
        if (sysLog == null) {
            return Result.error("操作日志不存在");
        }
        return Result.data(sysLog);
    }
}
