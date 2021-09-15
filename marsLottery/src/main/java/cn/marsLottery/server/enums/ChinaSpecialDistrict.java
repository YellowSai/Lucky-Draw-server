package cn.marsLottery.server.enums;

import cn.jzcscw.commons.core.IdNameEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ChenJiaHui
 */

@Getter
public enum ChinaSpecialDistrict implements IdNameEnum {

    /**
     * 香港
     */
    HONG_KONG(10810000, "香港"),

    /**
     * 澳门
     */
    MACAO(10820000, "澳门"),

    /**
     * 台湾
     */
    TAI_WAN(10710000, "台湾");

    /**
     * sysCity对应的id
     */
    private int value;

    /**
     * 对应文本
     */
    private String name;

    ChinaSpecialDistrict(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static List<Integer> getIdList() {
        return Arrays.stream(ChinaSpecialDistrict.values()).map(ChinaSpecialDistrict::getValue).collect(Collectors.toList());
    }
}
