package cn.jzcscw.commons.constant;

import cn.jzcscw.commons.core.IdNameEnum;

/**
 * 数据状态枚举
 *
 */
public enum DataStatus implements IdNameEnum {

    /**
     * 状态类型定义
     */

    NORMAL(2, "正常"),

    DEL(3, "删除");

    /**
     * int值
     */
    private int value;

    /**
     * 对应文本
     */
    private String name;

    DataStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getValue() {
        return value;
    }

}
