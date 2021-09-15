package cn.jzcscw.third.weixin.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import cn.jzcscw.commons.cache.CacheManager;
import cn.jzcscw.commons.util.HttpsUtil;
import cn.jzcscw.third.weixin.bean.WeixinAuthResult;
import cn.jzcscw.third.weixin.bean.WeixinMiniProgramUserInfo;
import cn.jzcscw.third.weixin.bean.WeixinPhoneInfo;
import cn.jzcscw.third.weixin.config.WeixinConfig;
import cn.jzcscw.third.weixin.service.WeixinMiniProgramService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Service
public class WeixinMiniProgramServiceImpl implements WeixinMiniProgramService {

    private static boolean initialized = false;

    private String sessionKeyPrefix = "wxProgramSessionKey_";

    private static String code2SessionUrl = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private CacheManager cacheManager;


    @Override
    public String getSessionKey(WeixinConfig weixinConfig, String openId) {
        String key = sessionKeyPrefix + openId;
        WeixinAuthResult weixinAuthResult = (WeixinAuthResult) cacheManager.get(key);
        return weixinAuthResult == null ? "" : weixinAuthResult.getSession_key();
    }

    @Override
    public WeixinAuthResult code2Session(WeixinConfig weixinConfig, String code) {
        String url = code2SessionUrl + "?appid=" + weixinConfig.getAppId() + "&secret=" + weixinConfig.getAppSecret() + "&js_code=" + code + "&grant_type=authorization_code";
        String json = HttpsUtil.doGet(url);
        try {
            WeixinAuthResult weixinAuthResult = JSONUtil.toBean(json, WeixinAuthResult.class);
            cacheManager.set(sessionKeyPrefix + weixinAuthResult.getOpenid(), weixinAuthResult);
            return weixinAuthResult;
        } catch (Exception e) {
            log.error("code2Session error {}", e);
        }

        return null;
    }

    @Override
    public boolean signatureValid(WeixinConfig weixinConfig, String openid, String rawData, String signature) {
        String sessionKey = getSessionKey(weixinConfig, openid);
        if (sessionKey == null) {
            return false;
        }
        String sign = SecureUtil.sha1(rawData + sessionKey);
        return sign.equals(signature);
    }

    @Override
    public WeixinMiniProgramUserInfo decrypt(WeixinConfig weixinConfig, String openid, String encryptedData, String iv) {
        /**
         * 对称解密使用的算法为 AES-128-CBC，数据采用PKCS#7填充。
         * 对称解密的目标密文为 Base64_Decode(encryptedData)。
         * 对称解密秘钥 aeskey = Base64_Decode(session_key), aeskey 是16字节。
         * 对称解密算法初始向量 为Base64_Decode(iv)，其中iv由数据接口返回。
         */
        WeixinAuthResult weixinAuthResult = this.getWeixinAuthResult(openid);
        if (weixinAuthResult == null) {
            return null;
        }

        try {
            byte[] resultByte = decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(weixinAuthResult.getSession_key()), Base64.decodeBase64(iv));
            if (null != resultByte && resultByte.length > 0) {
                String result = decode(resultByte);
                log.debug("result->{}", result);

                WeixinMiniProgramUserInfo userInfo = new WeixinMiniProgramUserInfo(JSONUtil.toBean(result, Map.class));
                userInfo.setUnionid(weixinAuthResult.getUnionid());
                userInfo.setOpenid(weixinAuthResult.getOpenid());
                return userInfo;
            }
        } catch (Exception e) {
            log.error("decrypt err {}", e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public WeixinPhoneInfo decryptPhoneNumber(WeixinConfig weixinConfig, String openid, String encryptedData, String iv) {
        WeixinAuthResult weixinAuthResult = this.getWeixinAuthResult(openid);
        if (weixinAuthResult == null) {
            return null;
        }

        try {
            byte[] resultByte = decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(weixinAuthResult.getSession_key()), Base64.decodeBase64(iv));
            if (null != resultByte && resultByte.length > 0) {
                String result = decode(resultByte);
                log.debug("result->{}", result);
                return JSONUtil.toBean(result, WeixinPhoneInfo.class);

            }
        } catch (Exception e) {
            log.error("decryptPhoneNumber err {}", e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public WeixinAuthResult getWeixinAuthResult(String openid) {
        return (WeixinAuthResult) cacheManager.get(sessionKeyPrefix + openid);
    }

    private byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) {
        try {
            if (!initialized) {
                Security.addProvider(new BouncyCastleProvider());
                initialized = true;
            }
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key sKeySpec = new SecretKeySpec(keyByte, "AES");

            AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
            params.init(new IvParameterSpec(ivByte));

            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, params);// 初始化
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            log.error("decrypt err {}", e);
        }
        return null;
    }

    private static String decode(byte[] decrypted) {
        int pad = decrypted[decrypted.length - 1];
        if (pad < 1 || pad > 32) {
            pad = 0;
        }
        return new String(Arrays.copyOfRange(decrypted, 0, decrypted.length - pad));
    }
}
