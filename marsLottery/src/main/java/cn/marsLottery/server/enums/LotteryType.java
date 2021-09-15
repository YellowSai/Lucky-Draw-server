package cn.marsLottery.server.enums;

/**
 * @author HuangYuHan
 */

public enum LotteryType {

    /**
     * 活动关闭
     */
    CLOSE("N", "关闭"),
    /**
     * 活动开启
     */
    OPEN("Y", "开启");

    /**
     * string值
     */
    private String value;

    /**
     * 对应文本
     */
    private String name;

    LotteryType(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }
}
