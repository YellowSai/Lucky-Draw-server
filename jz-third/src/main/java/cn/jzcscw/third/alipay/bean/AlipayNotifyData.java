package cn.jzcscw.third.alipay.bean;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AlipayNotifyData {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date notify_time;

    private String notify_type;
    private String notify_id;
    private String app_id;
    private String charset;
    private String version;
    private String sign_type;
    private String sign;
    private String trade_no;
    private String out_trade_no;
    private String out_biz_no;
    private String buyer_id;
    private String buyer_logon_id;
    private String seller_id;
    private String seller_email;
    private String trade_status;
    private BigDecimal total_amount;
    private BigDecimal receipt_amount;
    private BigDecimal invoice_amount;
    private BigDecimal buyer_pay_amount;
    private BigDecimal point_amount;
    private BigDecimal refund_fee;
    private String subject;
    private String body;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmt_create;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmt_payment;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmt_refund;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmt_close;

    private String fund_bill_list;
    private String passback_params;
    private String voucher_detail_list;
}
