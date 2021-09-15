package cn.jzcscw.third.alipay.service.impl;

import cn.jzcscw.commons.exception.JzRuntimeException;
import cn.jzcscw.third.alipay.bean.AlipayConfig;
import cn.jzcscw.third.alipay.service.AlipayService;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.app.models.AlipayTradeAppPayResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.alipay.easysdk.payment.wap.models.AlipayTradeWapPayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author jianzheng
 */
@Slf4j
@Service
public class AlipayServiceImpl implements AlipayService {

    @Override
    public void setOptions(AlipayConfig alipayConfig) {
        try {
            Factory.setOptions(this.getOptions(alipayConfig));
        } catch (Exception e) {
            log.error("alipay init fail:{}", e);
        }
    }

    @Override
    public Config getOptions(AlipayConfig alipayConfig) {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipay.com";
        config.signType = alipayConfig.getSignType();

        config.appId = alipayConfig.getAppId();
        config.merchantPrivateKey = alipayConfig.getAppPrivateKey();

        //注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
//        config.merchantCertPath = "appCertPublicKey_2021001193611212.crt";
//        config.alipayCertPath = "alipayCertPublicKey_RSA2.crt";
//        config.alipayRootCertPath = "alipayRootCert.crt";

        //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.alipayPublicKey = alipayConfig.getAlipayPublicKey();

        config.notifyUrl = alipayConfig.getNotifyUrl();
        return config;
    }

    @Override
    public String wapUnifiedOrder(String subject, String outTradeNo, BigDecimal amount, String quitUrl, String returnUrl) {
        try {
            log.info("wapUnifiedOrder: subject[{}],outTradeNo[{}],amount[{}],quitUrl[{}],returnUrl[{}]" + subject, amount, quitUrl, returnUrl);
            AlipayTradeWapPayResponse response = Factory.Payment.Wap().pay(subject, outTradeNo, amount.toString(), quitUrl, returnUrl);
            if (ResponseChecker.success(response)) {
                log.debug("response =>{}", response.body);
                return response.body;
            } else {
                log.error("webUnifiedOrder fail:{}", response.body);
            }
        } catch (Exception e) {
            log.error("webUnifiedOrder fail,{}", e);
        }
        return null;
    }

    @Override
    public String webUnifiedOrder(String subject, String outTradeNo, BigDecimal amount, String returnUrl) {
        try {
            log.info("webUnifiedOrder: subject[{}],outTradeNo[{}],amount[{}],returnUrl[{}]" + subject, amount, returnUrl);
            AlipayTradePagePayResponse response = Factory.Payment.Page().pay(subject, outTradeNo, amount.toString(), returnUrl);
            if (ResponseChecker.success(response)) {
                log.debug("response =>{}", response.body);
                return response.body;
            } else {
                log.error("webUnifiedOrder fail:{}", response.body);
            }
        } catch (Exception e) {
            log.error("webUnifiedOrder fail,{}", e);
        }
        return null;
    }

    @Override
    public AlipayTradeRefundResponse refund(String payTradeNo, BigDecimal payTotal) {
        try {
            AlipayTradeRefundResponse response = Factory.Payment.Common().refund(payTradeNo, payTotal.toString());
            return response;
        } catch (Exception e) {
            log.error("send refund fail:{}", e);
            throw new JzRuntimeException("退款失败，请稍后尝试");
        }
    }

    @Override
    public Boolean notifyVerified(Map<String, String> payNotifyMap) throws Exception {
        return Factory.Payment.Common().verifyNotify(payNotifyMap);
    }

    @Override
    public String appPay(String subject, String outTradeNo, BigDecimal amount) {
        try {
            log.info("subject:" + subject);
            log.info("outTradeNo:" + outTradeNo);
            log.info("amount:" + amount);
            AlipayTradeAppPayResponse response = Factory.Payment.App().pay(subject, outTradeNo, amount.toString());
            if (ResponseChecker.success(response)) {
                log.debug("response =>{}", response.body);
                return response.body;
            } else {
                log.error("webUnifiedOrder fail:{}", response.body);
            }
        } catch (Exception e) {
            log.error("webUnifiedOrder fail,{}", e);
        }
        return null;
    }

}
