package cn.marsLottery.server.aspect;

import cn.jzcscw.commons.core.Result;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class ControllerAspect {
    private static final String TRACE_ID = "traceId";

    private ThreadLocal<Long> timeLocal = new ThreadLocal<Long>();
    private ThreadLocal<String> methodLocal = new ThreadLocal<String>();

    @Pointcut("execution(public * cn.marsLottery.server.controller..*.*(..))")
    public void controllerMethod() {
    }

    @Before("controllerMethod()")
    public void doBefore(JoinPoint joinPoint) {
        timeLocal.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        methodLocal.set(request.getRequestURI());
    }

    @Around("controllerMethod()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //添加 MDC
        String traceId = IdWorker.get32UUID();
        MDC.put(TRACE_ID, traceId);
        Object result = point.proceed();
        if (result instanceof Result) {
            ((Result) result).setTraceId(traceId);
        }
        //移除 MDC
        MDC.remove(TRACE_ID);
        return result;
    }

    @AfterReturning(returning = "ret", pointcut = "controllerMethod()")
    public void doAfterReturning(Object ret) {
        long costTime = System.currentTimeMillis() - timeLocal.get();
        log.info("request[{}] done, cost: {}ms", methodLocal.get(), costTime);

        methodLocal.remove();
        timeLocal.remove();
    }

    @AfterThrowing(throwing = "ex", pointcut = "controllerMethod()")
    public void doAfterThrowing(Exception ex) {
        long costTime = System.currentTimeMillis() - timeLocal.get();
        log.error("request[{}] error, cost: {}ms", methodLocal.get(), costTime);

        methodLocal.remove();
        timeLocal.remove();
    }
}
