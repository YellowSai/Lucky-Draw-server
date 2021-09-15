package cn.marsLottery.server.enums;

import cn.jzcscw.commons.core.IdNameEnum;
import lombok.Getter;

/**
 * @author jianzheng
 */

@Getter
public enum UserRoleType implements IdNameEnum {

    /**
     * 普通用户
     */
    USER(0, ""),

    /**
     * 超级管理员
     */
    SUPER(1, "super"),

    /**
     * 管理员
     */
    ADMIN(2, "admin"),

    /**
     * 咨询师
     */
    BRAND(3, "brandSide");

    /**
     * role_id
     */
    private int value;

    /**
     * role
     */
    private String name;

    UserRoleType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static boolean inRole(String role) {
        UserRoleType[] types = UserRoleType.values();
        for (UserRoleType type : types) {
            if (type.name.equals(role)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAdmin(String role) {
        if (ADMIN.getName().equals(role)) {
            return true;
        }
        return false;
    }

    public static boolean isBrandSide(String role) {
        if (BRAND.getName().equals(role)) {
            return true;
        }
        return false;
    }

}
