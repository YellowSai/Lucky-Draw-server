package cn.jzcscw.third.alipay.bean;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AlipayReturnData {
    private String charset;
    private String out_trade_no;
    private String method;
    private BigDecimal total_amount;
    private String sign;
    private String trade_no;
    private String auth_app_id;
    private String version;
    private String app_id;
    private String sign_type;
    private String seller_id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;
}
