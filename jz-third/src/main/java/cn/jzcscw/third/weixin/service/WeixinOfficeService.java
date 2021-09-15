package cn.jzcscw.third.weixin.service;

import cn.jzcscw.third.weixin.bean.WeixinOfficeUserInfo;
import cn.jzcscw.third.weixin.bean.WeixinOfficeUserToken;
import cn.jzcscw.third.weixin.bean.WeixinTemplateRequestData;
import cn.jzcscw.third.weixin.bean.WeixinTemplateResult;
import cn.jzcscw.third.weixin.config.WeixinConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 微信公众号相关服务
 */
public interface WeixinOfficeService {

    /**
     * 获取code
     *
     * @param response
     * @param redirectUri
     * @param scope
     * @param state
     */
    void redirectForCode(WeixinConfig weixinConfig, HttpServletResponse response, String redirectUri, String scope, String state);

    WeixinOfficeUserToken getUserAccessToken(WeixinConfig weixinConfig, String code);

    WeixinOfficeUserInfo getUserInfo(String accessToken, String openId);

    WeixinOfficeUserInfo getUserInfo(WeixinConfig weixinConfig, String code);

    String signature(String nonceStr, String timestamp, String url, String jsApiTicket);

    /**
     * 公众号模板信息推送
     *
     * @param weixinConfig              WeixinConfig
     * @param weixinTemplateRequestData WeixinTemplateRequestData
     * @return WeixinTemplateResult
     */
    WeixinTemplateResult sendTemplateMessage(WeixinConfig weixinConfig, WeixinTemplateRequestData weixinTemplateRequestData);

    /**
     * @param miniProgramAppId miniProgramAppId
     * @param templateId       templateId
     * @param openId           openId
     * @param url              跳转url, 可以为空字符串
     * @param pagepath         小程序跳转url, 可以为空字符串
     * @param data             模板参数
     * @return WeixinTemplateRequestData
     */
    WeixinTemplateRequestData getWeixinTemplateRequestData(String miniProgramAppId, String templateId, String openId, String url, String pagepath, Map<String, WeixinTemplateRequestData.TemplateData> data);


}
