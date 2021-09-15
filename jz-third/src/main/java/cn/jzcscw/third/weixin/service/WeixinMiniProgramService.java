package cn.jzcscw.third.weixin.service;

import cn.jzcscw.third.weixin.bean.WeixinAuthResult;
import cn.jzcscw.third.weixin.bean.WeixinMiniProgramUserInfo;
import cn.jzcscw.third.weixin.bean.WeixinPhoneInfo;
import cn.jzcscw.third.weixin.config.WeixinConfig;

/**
 * 微信小程序相关服务
 */
public interface WeixinMiniProgramService {

    String getSessionKey(WeixinConfig weixinConfig, String openId);

    /**
     * 获取授权信息，包含openid和unionid
     *
     * @param weixinConfig
     * @param code
     * @return
     */
    WeixinAuthResult code2Session(WeixinConfig weixinConfig, String code);

    /**
     * 验证签名
     *
     * @param weixinConfig
     * @param openid
     * @param rawData
     * @param signature
     * @return
     */
    boolean signatureValid(WeixinConfig weixinConfig, String openid, String rawData, String signature);

    /**
     * 解密隐私加密数据
     *
     * @param weixinConfig
     * @param openid
     * @param encryptedData
     * @param iv
     * @return
     */
    WeixinMiniProgramUserInfo decrypt(WeixinConfig weixinConfig, String openid, String encryptedData, String iv);

    /**
     * 解密手机号加密数据
     *
     * @param weixinConfig  weixinConfig
     * @param openid        openid
     * @param encryptedData encryptedData
     * @param iv            iv
     * @return WeixinPhoneInfo
     */
    WeixinPhoneInfo decryptPhoneNumber(WeixinConfig weixinConfig, String openid, String encryptedData, String iv);

    WeixinAuthResult getWeixinAuthResult(String openid);
}
