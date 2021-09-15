package cn.jzcscw.third.weixin.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.json.JSONUtil;
import cn.jzcscw.commons.util.HttpClientUtil;
import cn.jzcscw.commons.util.HttpsUtil;
import cn.jzcscw.third.weixin.bean.WeixinPayH5UnifiedResponseData;
import cn.jzcscw.third.weixin.bean.WeixinPayH5UnifiedResquestData;
import cn.jzcscw.third.weixin.bean.WeixinPayNativeUnifiedResponseData;
import cn.jzcscw.third.weixin.bean.WeixinPayNativeUnifiedResquestData;
import cn.jzcscw.third.weixin.bean.WeixinPayRefundRequestData;
import cn.jzcscw.third.weixin.bean.WeixinPayRefundResponseData;
import cn.jzcscw.third.weixin.bean.WeixinPayJsUnifiedResponseData;
import cn.jzcscw.third.weixin.bean.WeixinPayJsUnifiedResquestData;
import cn.jzcscw.third.weixin.bean.WeixinTransferBankRequestData;
import cn.jzcscw.third.weixin.bean.WeixinTransferBankResponseData;
import cn.jzcscw.third.weixin.bean.WeixinTransferWalletRequestData;
import cn.jzcscw.third.weixin.bean.WeixinTransferWalletResponseData;
import cn.jzcscw.third.weixin.config.WeixinPayConfig;
import cn.jzcscw.third.weixin.service.WeixinPayService;
import cn.jzcscw.third.weixin.util.WeixinUtil;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    private static String jsUnifiedOrderV3Url = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";

    private static String h5UnifiedOrderV3Url = "https://api.mch.weixin.qq.com/v3/pay/transactions/h5";

    private static String nativeUnifiedOrderV3Url = "https://api.mch.weixin.qq.com/v3/pay/transactions/native";

    private static String refundV3Url = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds";

    private static String transferToWalletV2Url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    private static String transferToBankCardV2Url = "https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank";

    @Override
    public CloseableHttpClient getHttpClient(WeixinPayConfig weixinPayConfig) throws IOException {
        // 加载商户私钥（privateKey：私钥字符串）
        PrivateKey merchantPrivateKey = PemUtil
                .loadPrivateKey(new ByteArrayInputStream(weixinPayConfig.getMerchantPrivateKey().getBytes("utf-8")));

        // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3秘钥）
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(weixinPayConfig.getMchId(),
                        new PrivateKeySigner(weixinPayConfig.getMchSerialNo(), merchantPrivateKey)), weixinPayConfig.getApiV3Key().getBytes("utf-8"));

        // 初始化httpClient
        return WechatPayHttpClientBuilder.create()
                .withMerchant(weixinPayConfig.getMchId(), weixinPayConfig.getMchSerialNo(), merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier)).build();
    }

    @Override
    public WeixinPayJsUnifiedResquestData getUnifiedRequestData(WeixinPayConfig weixinPayConfig, String openId, String description, String outTradeNo, int totalFee, String ip, List<WeixinPayJsUnifiedResquestData.GoodsDetail> goodsDetail, int expireMinute) {
        String billExpireTime = DateUtil.format(DateUtil.offsetMinute(DateUtil.date(), expireMinute), DatePattern.UTC_PATTERN);

        return new WeixinPayJsUnifiedResquestData(weixinPayConfig.getAppId(), weixinPayConfig.getMchId(), openId,
                description, outTradeNo, billExpireTime, "", weixinPayConfig.getNotifyUrl(), "",
                totalFee, "CNY", ip, goodsDetail);
    }

    @Override
    public WeixinPayJsUnifiedResponseData requestApiUnifiedData(WeixinPayConfig weixinPayConfig, WeixinPayJsUnifiedResquestData weixinPayJsUnifiedResquestData) throws IOException {
        CloseableHttpClient client = getHttpClient(weixinPayConfig);
        String resp = HttpClientUtil.doPostJson(client, jsUnifiedOrderV3Url, null, weixinPayJsUnifiedResquestData);
        WeixinPayJsUnifiedResponseData weixinPayJsUnifiedResponseData = JSONUtil.toBean(resp, WeixinPayJsUnifiedResponseData.class);
        return weixinPayJsUnifiedResponseData;
    }

    @Override
    public WeixinPayH5UnifiedResquestData getH5UnifiedRequestData(WeixinPayConfig weixinPayConfig, String description, String outTradeNo, int totalFee, String ip, List<WeixinPayH5UnifiedResquestData.GoodsDetail> goodsDetail, int expireMinute) {
        String billExpireTime = DateUtil.format(DateUtil.offsetMinute(DateUtil.date(), expireMinute), DatePattern.UTC_PATTERN);

        return new WeixinPayH5UnifiedResquestData(weixinPayConfig.getAppId(), weixinPayConfig.getMchId(),
                description, outTradeNo, billExpireTime, "", weixinPayConfig.getNotifyUrl(), "",
                totalFee, "CNY", ip, goodsDetail);
    }

    @Override
    public WeixinPayH5UnifiedResponseData requestH5UnifiedData(WeixinPayConfig weixinPayConfig, WeixinPayH5UnifiedResquestData weixinPayH5UnifiedResquestData) throws IOException {
        CloseableHttpClient client = getHttpClient(weixinPayConfig);
        String resp = HttpClientUtil.doPostJson(client, h5UnifiedOrderV3Url, null, weixinPayH5UnifiedResquestData);
        WeixinPayH5UnifiedResponseData weixinPayH5UnifiedResponseData = JSONUtil.toBean(resp, WeixinPayH5UnifiedResponseData.class);
        return weixinPayH5UnifiedResponseData;
    }

    @Override
    public WeixinPayNativeUnifiedResquestData getNativeUnifiedRequestData(WeixinPayConfig weixinPayConfig, String description, String outTradeNo, int totalFee, String ip, List<WeixinPayNativeUnifiedResquestData.GoodsDetail> goodsDetail, int expireMinute) {
        String billExpireTime = DateUtil.format(DateUtil.offsetMinute(DateUtil.date(), expireMinute), DatePattern.UTC_PATTERN);

        return new WeixinPayNativeUnifiedResquestData(weixinPayConfig.getAppId(), weixinPayConfig.getMchId(),
                description, outTradeNo, billExpireTime, "", weixinPayConfig.getNotifyUrl(), "",
                totalFee, "CNY", ip, goodsDetail);
    }

    @Override
    public WeixinPayNativeUnifiedResponseData requestNativeUnifiedData(WeixinPayConfig weixinPayConfig, WeixinPayNativeUnifiedResquestData weixinPayNativeUnifiedResquestData) throws IOException {
        CloseableHttpClient client = getHttpClient(weixinPayConfig);
        String resp = HttpClientUtil.doPostJson(client, nativeUnifiedOrderV3Url, null, weixinPayNativeUnifiedResquestData);
        WeixinPayNativeUnifiedResponseData weixinPayNativeUnifiedResponseData = JSONUtil.toBean(resp, WeixinPayNativeUnifiedResponseData.class);
        return weixinPayNativeUnifiedResponseData;
    }

    @Override
    public WeixinPayRefundRequestData getRefundRequestData(WeixinPayConfig weixinPayConfig, String transactionId, String outTradeTradeNo, String outRefundNo, String reason, int refundFee, int totalFee) {
        return new WeixinPayRefundRequestData(transactionId, outTradeTradeNo, outRefundNo, reason, weixinPayConfig.getNotifyUrl(), null, refundFee, totalFee, "CNY");
    }

    @Override
    public WeixinPayRefundResponseData requestRefundData(WeixinPayConfig weixinPayConfig, WeixinPayRefundRequestData weixinPayRefundRequestData) throws IOException {
        CloseableHttpClient client = getHttpClient(weixinPayConfig);
        String resp = HttpClientUtil.doPostJson(client, refundV3Url, null, weixinPayRefundRequestData);
        WeixinPayRefundResponseData weixinPayRefundResponseData = JSONUtil.toBean(resp, WeixinPayRefundResponseData.class);
        return weixinPayRefundResponseData;
    }

    @Override
    public String paySign(WeixinPayConfig weixinPayConfig, Map<String, String> map) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(map.get("appId")).append("\n");
        stringBuffer.append(map.get("timeStamp")).append("\n");
        stringBuffer.append(map.get("nonceStr")).append("\n");
        stringBuffer.append(map.get("package")).append("\n");
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA, Base64.decode(weixinPayConfig.getMerchantPrivateKey()), null);
        byte[] crypto = sign.sign(stringBuffer.toString().getBytes(StandardCharsets.UTF_8));
        return Base64.encode(crypto);
    }

    @Override
    public String decryptoString(WeixinPayConfig weixinPayConfig, String associatedData, String nonce, String ciphertext) throws GeneralSecurityException, IOException {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            SecretKeySpec key = new SecretKeySpec(weixinPayConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8), "AES");
            GCMParameterSpec spec = new GCMParameterSpec(128, nonce.getBytes(StandardCharsets.UTF_8));

            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData.getBytes(StandardCharsets.UTF_8));

            return new String(cipher.doFinal(Base64.decode(ciphertext)), "utf-8");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public WeixinTransferWalletResponseData transferToWallet(WeixinPayConfig weixinPayConfig, WeixinTransferWalletRequestData weixinTransferWalletRequestData) {
        String xmlStr = WeixinUtil.toXmlStr(weixinTransferWalletRequestData, "UTF-8");
        String xml = HttpsUtil.doPostXml(transferToWalletV2Url, xmlStr);
        try {
            WeixinTransferWalletResponseData weixinTransferWalletResponseData = (WeixinTransferWalletResponseData) WeixinUtil.getObjectFromXML(xml, WeixinTransferWalletResponseData.class);
            return weixinTransferWalletResponseData;
        } catch (Exception e) {
            log.error("unifiedOrder error {}", e);
        }
        return null;
    }

    @Override
    public WeixinTransferBankResponseData transferToBankCard(WeixinPayConfig weixinPayConfig, WeixinTransferBankRequestData weixinTransferBankRequestData) {
        String xmlStr = WeixinUtil.toXmlStr(weixinTransferBankRequestData, "UTF-8");
        String xml = HttpsUtil.doPostXml(transferToBankCardV2Url, xmlStr);
        try {
            WeixinTransferBankResponseData weixinTransferWalletResponseData = (WeixinTransferBankResponseData) WeixinUtil.getObjectFromXML(xml, WeixinTransferBankResponseData.class);
            return weixinTransferWalletResponseData;
        } catch (Exception e) {
            log.error("unifiedOrder error {}", e);
        }
        return null;
    }

    @Override
    public WeixinTransferWalletRequestData getWalletRequestData(WeixinPayConfig weixinPayConfig, int amount, String partnerTradeNo, String name, String openId, String desc) {
        return new WeixinTransferWalletRequestData(weixinPayConfig, partnerTradeNo, openId, name, amount, desc);
    }

    @Override
    public WeixinTransferBankRequestData getBankRequestData(WeixinPayConfig weixinPayConfig, int amount, String partnerTradeNo, String name, String cardId, String bankCode, String desc) {
        return new WeixinTransferBankRequestData(weixinPayConfig, amount, partnerTradeNo, name, cardId, bankCode, desc);
    }


}
