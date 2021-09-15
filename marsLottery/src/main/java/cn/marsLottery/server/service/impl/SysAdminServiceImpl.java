package cn.marsLottery.server.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import cn.jzcscw.commons.constant.Consts;
import cn.jzcscw.commons.exception.JzRuntimeException;
import cn.jzcscw.commons.util.JWTUtil;
import cn.jzcscw.commons.cache.CacheManager;
import cn.marsLottery.server.config.AppConfig;
import cn.marsLottery.server.dao.SysAdminDao;
import cn.marsLottery.server.po.SysAdmin;
import cn.marsLottery.server.service.SysAdminService;
import cn.marsLottery.server.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * SysAdmin对应的服务类实现
 *
 * @author auto
 */

@Service("sysAdminService")
public class SysAdminServiceImpl extends ServiceImpl<SysAdminDao, SysAdmin> implements SysAdminService {

    //12小时不操作即退出
    private static int maxLife = 12 * 60 * 60;
    private static String loginPrefix = "admin_";

    @Autowired
    AppConfig appConfig;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysAdminService sysAdminService;

    @Override
    public SysAdmin getByToken(String token, boolean ignoreAuth) {
        Claims claims = JWTUtil.parseJWT(appConfig.getJwtSecret(), token);
        if (!ignoreAuth && JWTUtil.isExpired(claims, appConfig.getJwtSubject())) {
            throw new JzRuntimeException(Consts.NO_LOGIN_CODE, "未登录");
        }
        if (claims != null) {
            int adminId = Integer.parseInt(String.valueOf(claims.get("id")));
            boolean isLogin = sysAdminService.isLogin(adminId, token);
            if (!ignoreAuth && !isLogin) {
                throw new JzRuntimeException(Consts.NO_LOGIN_CODE, "未登录");
            }
            sysAdminService.renewLogin(adminId, token);
            return getById(adminId);
        }
        return null;
    }

    @Override
    public void login(int adminId, String token, String ip) {
        LambdaUpdateWrapper<SysAdmin> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysAdmin::getLoginDate, DateUtil.now()).set(SysAdmin::getLoginIp, ip)
                .eq(SysAdmin::getId, adminId);
        sysAdminService.update(updateWrapper);

        String key = loginPrefix + adminId;
        LinkedList<String> tokenList = (LinkedList<String>) cacheManager.get(key);
        if (tokenList == null) {
            tokenList = new LinkedList<>();
        }
        //限制5个地点登录
        if (tokenList.size() >= 5) {
            tokenList.poll();
        }
        tokenList.add(token);
        cacheManager.set(key, tokenList, maxLife);
    }

    @Override
    public void logout(int adminId, String token) {
        String key = loginPrefix + adminId;
        LinkedList<String> tokenList = (LinkedList<String>) cacheManager.get(key);
        if (tokenList != null) {
            tokenList.remove(token);
        }
        cacheManager.set(key, tokenList, maxLife);
    }

    @Override
    public boolean isLogin(int adminId, String token) {
        String key = loginPrefix + adminId;
        LinkedList<String> tokenList = (LinkedList<String>) cacheManager.get(key);
        if (tokenList != null) {
            return tokenList.contains(token);
        }
        return false;
    }

    @Override
    public void renewLogin(int adminId, String token) {
        String key = loginPrefix + adminId;
        cacheManager.expire(key, maxLife);
    }

    @Override
    public SysAdmin getByUserName(String userName) {
        return getOne(new LambdaQueryWrapper<SysAdmin>().eq(SysAdmin::getUsername, userName));
    }

    @Override
    public String createJWT(SysAdmin sysAdmin) {
        if (sysAdmin == null) {
            return null;
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", sysAdmin.getId());
        claims.put("username", sysAdmin.getUsername());
        claims.put("mobile", sysAdmin.getMobile());
        claims.put("nickname", sysAdmin.getNickname());
        claims.put("email", sysAdmin.getEmail());
        String token = JWTUtil.createJWT(claims, appConfig.getJwtSubject(), appConfig.getJwtSecret(), appConfig.getJwtLife());
        return token;
    }

    @Override
    public SysAdmin getByMobile(String mobile) {
        return getOne(new LambdaQueryWrapper<SysAdmin>().eq(SysAdmin::getMobile, mobile));
    }

    @Override
    public String encodePassword(String password) {
        String salted = password;
        byte[] digest = HexUtil.decodeHex(SecureUtil.sha256(salted));
        byte[] saltByteArr = salted.getBytes();

        for (int i = 1; i < 5000; i++) {
            digest = ArrayUtil.addAll(digest, saltByteArr);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(digest);
            digest = HexUtil.decodeHex(SecureUtil.sha256(byteArrayInputStream));
        }
        return Base64.encode(digest);
    }

    @Override
    public SysAdmin getByMobileAndUsername(String mobile, String username) {
        return getOne(new LambdaQueryWrapper<SysAdmin>()
                .and(a -> a.eq(SysAdmin::getMobile, mobile))
                .or(b -> b.eq(SysAdmin::getUsername, username))
        );
    }
}
