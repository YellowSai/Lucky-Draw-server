package cn.marsLottery.server.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author auto
 */
@Data
public class OtherConfig implements Serializable {

    /**
     * 预约失败科普文章链接
     */
    String articleUrl;

}
