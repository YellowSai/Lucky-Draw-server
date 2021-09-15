package cn.jzcscw.third.weixin.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class WeixinPayConfig implements Serializable {
    /**
     * 名称
     */
    private String name;

    /**
     * appid
     */
    private String appId;
    /**
     * 商户号
     */
    private String mchId;
    /**
     * api密钥
     */
    private String apiKey;

    /**
     * 商户私钥,pkcs8格式，通过以下命令转换
     * openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in apiclient_key.pem -out apiclient_key_pkcs8.key
     */
    private String merchantPrivateKey;

    /**
     * 安全证书路径
     */
    private String apiClientCert;
    /**
     * 异步通知地址
     */
    private String notifyUrl;

    /**
     * 证书编号,获取方式
     * openssl x509 -in apiclient_cert.pem -noout -serial
     */

    private String mchSerialNo;

    /**
     * apiV3密钥，暂时未用到
     */
    private String apiV3Key;
}
