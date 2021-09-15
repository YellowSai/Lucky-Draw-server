package cn.marsLottery.server.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ChenJiaHui
 */
@Data
public class AllocationConfig implements Serializable {

    /**
     * 平台分成比例
     */
    BigDecimal platform;

    /**
     * 咨询师个人分成比例
     */
    BigDecimal personal;

}
