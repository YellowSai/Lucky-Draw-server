package cn.jzcscw.third.weixin.bean;

import lombok.Data;

@Data
public class WeixinTransferBankResponseData {
    // SUCCESS/FAIL
    private String return_code;
    //错误提示
    private String return_msg;

    //return_code为SUCCESS的时候有返回
    //业务结果
    private String result_code;

    //错误代码
    private String err_code;

    //错误代码描述
    private String err_code_des;

    //商户号
    private String mchid;

    private String partner_trade_no;

    private int amount;

    private String nonce_str;

    private String sign;

    //return_code 和result_code都为SUCCESS的时候有返回

    // 微信企业付款单号
    private String payment_no;

    //手续费金额
    private int cmms_amt;

}
