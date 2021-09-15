package cn.jzcscw.commons.constant;

import lombok.Getter;

/**
 * 管理员账号状态枚举
 */
@Getter
public enum AdminStatus {

    /**
     * 管理员账号状态定义
     */

    NORMAL("1", "正常"),

    DISABLE("2", "停用");

    /**
     * int值
     */
    private String value;

    /**
     * 对应文本
     */
    private String name;

    AdminStatus(String value, String name) {
        this.value = value;
        this.name = name;
    }

    static public boolean isNormal(String value) {
        return value.equals(NORMAL.value);
    }

    static public boolean isDisable(String value) {
        return value.equals(DISABLE.value);
    }

}
