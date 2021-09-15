package cn.jzcscw.third.weixin.service;

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
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

/**
 * 微信支付相关服务
 */
public interface WeixinPayService {

    CloseableHttpClient getHttpClient(WeixinPayConfig weixinPayConfig) throws IOException;

    WeixinPayJsUnifiedResquestData getUnifiedRequestData(WeixinPayConfig weixinPayConfig, String openId, String description, String outTradeNo, int totalFee, String ip, List<WeixinPayJsUnifiedResquestData.GoodsDetail> goodsDetail, int expireMinute);

    /**
     * jsapi统一下单
     *
     * @param weixinPayJsUnifiedResquestData
     * @return
     */
    WeixinPayJsUnifiedResponseData requestApiUnifiedData(WeixinPayConfig weixinPayConfig, WeixinPayJsUnifiedResquestData weixinPayJsUnifiedResquestData) throws IOException;

    WeixinPayH5UnifiedResquestData getH5UnifiedRequestData(WeixinPayConfig weixinPayConfig, String description, String outTradeNo, int totalFee, String ip, List<WeixinPayH5UnifiedResquestData.GoodsDetail> goodsDetail, int expireMinute);

    /**
     * h5统一下单
     *
     * @param weixinPayUnifiedResquestData
     * @return
     */
    WeixinPayH5UnifiedResponseData requestH5UnifiedData(WeixinPayConfig weixinPayConfig, WeixinPayH5UnifiedResquestData weixinPayUnifiedResquestData) throws IOException;

    WeixinPayNativeUnifiedResquestData getNativeUnifiedRequestData(WeixinPayConfig weixinPayConfig, String description, String outTradeNo, int totalFee, String ip, List<WeixinPayNativeUnifiedResquestData.GoodsDetail> goodsDetail, int expireMinute);

    /**
     * native统一下单
     *
     * @param weixinPayNativeUnifiedResquestData
     * @return
     */
    WeixinPayNativeUnifiedResponseData requestNativeUnifiedData(WeixinPayConfig weixinPayConfig, WeixinPayNativeUnifiedResquestData weixinPayNativeUnifiedResquestData) throws IOException;

    WeixinPayRefundRequestData getRefundRequestData(WeixinPayConfig weixinPayConfig, String transactionId, String outTradeTradeNo, String outRefundNo, String reason, int refundFee, int totalFee);

    /**
     * 退款
     *
     * @param weixinPayRefundRequestData
     * @return
     */
    WeixinPayRefundResponseData requestRefundData(WeixinPayConfig weixinPayConfig, WeixinPayRefundRequestData weixinPayRefundRequestData) throws IOException;

    String paySign(WeixinPayConfig weixinPayConfig, Map<String, String> map);

    /**
     * 解密v3接口的信息
     *
     * @param associatedData
     * @param nonce
     * @param ciphertext
     * @return
     */
    String decryptoString(WeixinPayConfig weixinPayConfig, String associatedData, String nonce, String ciphertext) throws GeneralSecurityException, IOException;

    /**
     * 转账到微信零钱
     *
     * @param weixinPayConfig
     * @param weixinTransferWalletRequestData
     * @return
     */
    WeixinTransferWalletResponseData transferToWallet(WeixinPayConfig weixinPayConfig, WeixinTransferWalletRequestData weixinTransferWalletRequestData);

    /**
     * 转账到银行卡
     *
     * @param weixinPayConfig
     * @param weixinTransferBankRequestData
     * @return
     */
    WeixinTransferBankResponseData transferToBankCard(WeixinPayConfig weixinPayConfig, WeixinTransferBankRequestData weixinTransferBankRequestData);

    WeixinTransferWalletRequestData getWalletRequestData(WeixinPayConfig weixinPayConfig, int amount, String partnerTradeNo, String name, String openId, String desc);

    WeixinTransferBankRequestData getBankRequestData(WeixinPayConfig weixinPayConfig, int amount, String partnerTradeNo, String name, String cardId, String bankCode, String desc);
}
