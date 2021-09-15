package cn.jzcscw.third.weixin.bean;

import lombok.Data;

import java.util.Date;

@Data
public class WeixinTransferWalletResponseData {
    // SUCCESS/FAIL
    private String return_code;
    //错误提示
    private String return_msg;
    //return_code为SUCCESS的时候有返回

    // 商户appid
    private String mch_appid;

    //商户号
    private String mchid;

    //设备号
    private String device_info;

    private String nonce_str;

    //业务结果
    private String result_code;

    //错误代码
    private String err_code;

    //错误代码描述
    private String err_code_des;


    //return_code 和result_code都为SUCCESS的时候有返回
    // 商户订单号
    private String partner_trade_no;

    // 微信付款单号
    private String payment_no;

    //付款成功时间
    private Date payment_time;

}
