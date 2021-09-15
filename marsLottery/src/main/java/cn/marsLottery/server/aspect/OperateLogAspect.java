package cn.marsLottery.server.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import cn.jzcscw.commons.annotation.OperateLog;
import cn.jzcscw.commons.constant.Consts;
import cn.jzcscw.commons.core.Result;
import cn.jzcscw.commons.web.RequestUtil;
import cn.marsLottery.server.po.SysLog;
import cn.marsLottery.server.vo.AdminLoginResult;
import cn.marsLottery.server.web.AdminWebContext;
import cn.marsLottery.server.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class OperateLogAspect {
    private ThreadLocal<SysLog> logLocal = new ThreadLocal<SysLog>();

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("execution(public * cn.marsLottery.server.controller.admapi..*.*(..))")
    public void sysLog() {
    }

    @Before("sysLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String path = request.getRequestURI();
        String ip = RequestUtil.getRemoteAddr(request);
        String query = JSONUtil.toJsonStr(request.getParameterMap());
        String data = JSONUtil.toJsonStr(joinPoint.getArgs());

        String methodName = joinPoint.getSignature().getName();
        Method method = currentMethod(joinPoint, methodName);
        OperateLog operateLog = method.getAnnotation(OperateLog.class);
        if (operateLog != null) {
            SysLog sysLog = new SysLog();
            sysLog.setUserId(AdminWebContext.getAdminId());
            sysLog.setUserName(AdminWebContext.getAdminName());
            sysLog.setModule(operateLog.module());
            sysLog.setDescription(operateLog.description());
            sysLog.setContent(String.format("uri:[%s], query:[%s], data:[%s]", path, query, data));
            sysLog.setIp(ip);
            sysLog.setCreateTime(DateUtil.date());
            logLocal.set(sysLog);
        }
    }

    @AfterReturning(returning = "ret", pointcut = "sysLog()")
    public void doAfterReturning(Object ret) {
        SysLog sysLog = logLocal.get();
        if (sysLog != null) {
            if (ret instanceof Result) {
                Result result = (Result) ret;
                if (result.getCode() == Consts.SUCCESS_CODE) {
                    //操作成功
                    Object data = result.getData();
                    if (data instanceof AdminLoginResult) {
                        AdminLoginResult loginResult = (AdminLoginResult) data;
                        sysLog.setUserId(loginResult.getAdmin().getId());
                        sysLog.setUserName(loginResult.getAdmin().getUsername());
                    }
                    try {
                        sysLogService.save(sysLog);
                    } catch (Exception e) {
                        log.error("syslog save fail {}", e);
                    }
                } else {
                    //操作失败
                }
            }
        }

        logLocal.remove();
    }

    private Method currentMethod(JoinPoint joinPoint, String methodName) {
        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        Method resultMethod = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
