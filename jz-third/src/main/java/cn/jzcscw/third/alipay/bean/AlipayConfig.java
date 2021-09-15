package cn.jzcscw.third.alipay.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class AlipayConfig implements Serializable {
    /**
     * 支付宝appid
     */
    private String appId;
    /**
     * 开发者应用私钥
     */
    private String appPrivateKey;
    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;
    /**
     * 签名方式,RSA2
     */
    private String signType = "RSA2";
    /**
     * 异步通知地址
     */
    private String notifyUrl;
}
