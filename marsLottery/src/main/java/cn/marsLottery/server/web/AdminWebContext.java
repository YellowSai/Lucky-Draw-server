package cn.marsLottery.server.web;

import lombok.Data;

/**
 * 存放管理后台web的上下文信息，主要是API接口的公共参数
 *
 * @author tanghuang 2020年02月25日
 */
public class AdminWebContext {

    private static final ThreadLocal<PublicParameter> publicParameterHolder = new
            ThreadLocal<PublicParameter>() {
                @Override
                protected PublicParameter initialValue() {
                    return new PublicParameter();
                }
            };

    /**
     * 获得公共参数
     */
    public static PublicParameter getPublicParameter() {
        return publicParameterHolder.get();
    }

    /**
     * 清除
     */
    public static void clear() {
        publicParameterHolder.remove();
    }

    /**
     * 用户IP
     */
    public static String getIp() {
        PublicParameter p = publicParameterHolder.get();
        return p.getIp();
    }

    /**
     * token
     */
    public static String getToken() {
        PublicParameter p = publicParameterHolder.get();
        return p.getToken();
    }

    public static int getAdminId() {
        PublicParameter p = publicParameterHolder.get();
        return p.getAdminId();
    }

    public static String getAdminName() {
        PublicParameter p = publicParameterHolder.get();
        return p.getAdminName();
    }

    public static String getRole() {
        PublicParameter p = publicParameterHolder.get();
        return p.getRole();
    }

    /**
     * 公共参数列表
     */
    @Data
    public static class PublicParameter {

        /**
         * 访问token, 系统颁发，登录后必须传入，所有用户信息将只能通过token换取
         */
        private String token;

        /**
         * 用户IP
         */
        private String ip;

        /**
         * 管理员id
         */
        private int adminId;

        /**
         * 管理员账号
         */
        private String adminName;

        /**
         * 角色类型
         */
        private String role;

        /**
         * 手机号
         */
        private String mobile;
    }
}
