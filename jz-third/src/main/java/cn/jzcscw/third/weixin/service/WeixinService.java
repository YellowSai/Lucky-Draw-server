package cn.jzcscw.third.weixin.service;

import cn.jzcscw.third.weixin.config.WeixinConfig;

/**
 * 微信公众号相关服务
 */
public interface WeixinService {

    String getAccessToken(WeixinConfig weixinConfig);

    String getApiTicket(WeixinConfig weixinConfig, String accessToken);

    String getApiTicket(WeixinConfig weixinConfig);

    String signature(String nonceStr, String timestamp, String url, String jsApiTicket);

}
