package cn.marsLottery.server.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author auto
 */
@Data
public class WithdrawalConfig implements Serializable {

    /**
     * 提现金额限制
     */
    BigDecimal withdrawalAmount;

}
