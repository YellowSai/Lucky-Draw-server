package cn.marsLottery.server.service;

import cn.marsLottery.server.po.SysAdmin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 表[sys_admin]对应的服务类
 *
 * @author auto
 */

public interface SysAdminService extends IService<SysAdmin> {
    SysAdmin getByToken(String token,boolean ignoreAuth);

    void login(int adminId, String token, String ip);

    void logout(int adminId, String token);

    boolean isLogin(int adminId, String token);

    void renewLogin(int adminId, String token);

    SysAdmin getByUserName(String userName);

    String createJWT(SysAdmin sysAdmin);

    SysAdmin getByMobile(String mobile);

    String encodePassword(String password);

    SysAdmin getByMobileAndUsername(String mobile, String username);
}
