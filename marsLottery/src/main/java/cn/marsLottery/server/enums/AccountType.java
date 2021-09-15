package cn.marsLottery.server.enums;

import cn.jzcscw.commons.core.IdNameEnum;
import lombok.Getter;

/**
 * @author ChenJiaHui
 */

@Getter
public enum AccountType implements IdNameEnum {

    /**
     * 账号类型
     */
    MOBILE(1, "手机号"),

    EMAIL(2, "邮箱");

    /**
     * int值
     */
    private int value;

    /**
     * 对应文本
     */
    private String name;

    AccountType(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
