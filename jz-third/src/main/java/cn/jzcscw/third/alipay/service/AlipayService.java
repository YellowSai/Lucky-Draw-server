package cn.jzcscw.third.alipay.service;

import cn.jzcscw.third.alipay.bean.AlipayConfig;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付宝支付相关服务
 */
public interface AlipayService {

    void setOptions(AlipayConfig alipayConfig);

    Config getOptions(AlipayConfig alipayConfig);

    /**
     * 手机网站支付
     *
     * @param subject
     * @param outTradeNo
     * @param amount
     * @param quitUrl
     * @param returnUrl
     * @return
     */
    String wapUnifiedOrder(String subject, String outTradeNo, BigDecimal amount, String quitUrl, String returnUrl);

    /**
     * pc网站支付
     *
     * @param subject
     * @param outTradeNo
     * @param amount
     * @param returnUrl
     * @return
     */
    String webUnifiedOrder(String subject, String outTradeNo, BigDecimal amount,  String returnUrl);
    /**
     * 申请退款
     *
     * @param payTradeNo
     * @param payTotal
     * @return
     */
    AlipayTradeRefundResponse refund(String payTradeNo, BigDecimal payTotal);

    Boolean notifyVerified(Map<String, String> payNotifyMap) throws Exception;

    String appPay(String subject, String outTradeNo, BigDecimal amount);
}
