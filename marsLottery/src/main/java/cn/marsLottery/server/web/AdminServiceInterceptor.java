package cn.marsLottery.server.web;

import cn.hutool.json.JSONUtil;
import cn.jzcscw.commons.annotation.ApiAuth;
import cn.jzcscw.commons.constant.Consts;
import cn.jzcscw.commons.core.Result;
import cn.jzcscw.commons.util.JWTUtil;
import cn.jzcscw.commons.util.StringUtil;
import cn.jzcscw.commons.web.RequestUtil;
import cn.marsLottery.server.service.SysAdminService;
import cn.marsLottery.server.config.AppConfig;
import cn.marsLottery.server.consts.AuthConsts;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 管理后台业务相关拦截器
 *
 * @author tanghuang 2020年02月25日
 */
@Slf4j
public class AdminServiceInterceptor implements HandlerInterceptor {

    private static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private SysAdminService sysAdminService;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     *
     * @param request
     * @param response
     * @param handler
     * @return true，如果false，停止流程，api被拦截
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean ignoreAuth = false;
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ApiAuth classAuth = handlerMethod.getMethod().getDeclaringClass().getAnnotation(ApiAuth.class);
            ignoreAuth = classAuth != null && classAuth.ignore();

            ApiAuth methodAuth = handlerMethod.getMethodAnnotation(ApiAuth.class);
            if (methodAuth != null) {
                //优先方法注解
                ignoreAuth = methodAuth.ignore();
            }
        } else {
            setResponse(response, Result.error(Consts.NOT_FOUND, "404 not found"));
            return false;
        }

        // 填充公共参数
        String bearToken = request.getHeader(AuthConsts.AUTHORIZATION_HEADER);
        if (StringUtils.isEmpty(bearToken)) {
            bearToken = request.getParameter(AuthConsts.AUTHORIZATION_HEADER);
        }

        AdminWebContext.PublicParameter publicParameter = AdminWebContext.getPublicParameter();
        String ip = RequestUtil.getRemoteAddr(request);
        publicParameter.setIp(ip);

        publicParameter.setToken(bearToken);

        if (!ignoreAuth && StringUtil.isEmpty(bearToken)) {
            setResponse(response, Result.error(Consts.NO_LOGIN_CODE, "未登录"));
            return false;
        }
        // 检查token
        Claims claims = JWTUtil.parseJWT(appConfig.getJwtSecret(), bearToken);
        if (!ignoreAuth && JWTUtil.isExpired(claims, appConfig.getJwtSubject())) {
            setResponse(response, Result.error(Consts.NO_LOGIN_CODE, "未登录"));
            return false;
        }

        if (claims != null) {
            int adminId = Integer.parseInt(String.valueOf(claims.get("id")));
            boolean isLogin = sysAdminService.isLogin(adminId, bearToken);
            if (!isLogin) {
                setResponse(response, Result.error(Consts.NO_LOGIN_CODE, "未登录"));
                return false;
            } else {
                sysAdminService.renewLogin(adminId, bearToken);
            }
            String mobile = String.valueOf(claims.get("mobile"));
            publicParameter.setAdminId(adminId);
            publicParameter.setMobile(mobile);
        }

        return true;
    }

    private void setResponse(ServletResponse response, Result result) {
        try {
            OutputStream os = response.getOutputStream();
            String jsonStr = JSONUtil.toJsonStr(result);
            os.write(jsonStr.getBytes(StandardCharsets.UTF_8));
            response.setContentType(JSON_CONTENT_TYPE);
        } catch (Exception ex) {
            log.warn("exception=" + ex);
        }
    }
}
