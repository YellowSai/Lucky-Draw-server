package cn.jzcscw.third.weixin.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import cn.jzcscw.commons.cache.CacheManager;
import cn.jzcscw.commons.util.HttpsUtil;
import cn.jzcscw.commons.util.StringUtil;
import cn.jzcscw.third.weixin.bean.WeixinOfficeAccessToken;
import cn.jzcscw.third.weixin.bean.WeixinOfficeJsApiTicket;
import cn.jzcscw.third.weixin.config.WeixinConfig;
import cn.jzcscw.third.weixin.service.WeixinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WeixinServiceImpl implements WeixinService {

    private static String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";

    private static String jsapiTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

    private String accessTokenKey = "accessToken";

    private String jsApiTicketKey = "jsApiTicket";


    @Autowired
    private CacheManager cacheManager;

    @Override
    public String getAccessToken(WeixinConfig weixinConfig) {
        String key = accessTokenKey + "_" + weixinConfig.getAppId();
        String accessToken = (String) cacheManager.get(key);
        if (StringUtil.isEmpty(accessToken)) {
            String url = accessTokenUrl + "?grant_type=client_credential&appid=" + weixinConfig.getAppId() + "&secret=" + weixinConfig.getAppSecret();
            String json = HttpsUtil.doGet(url);
            log.info("getAccessToken=>{}", json);
            WeixinOfficeAccessToken token = JSONUtil.toBean(json, WeixinOfficeAccessToken.class);
            if (token != null && StringUtil.isNotEmpty(token.getAccess_token())) {
                accessToken = token.getAccess_token();
                //提前一分钟过期
                cacheManager.set(key, accessToken, token.getExpires_in() - 60);
            } else {
                //重试获取
//                return this.getAccessToken(weixinConfig);
            }
        }
        return accessToken;
    }

    @Override
    public String getApiTicket(WeixinConfig weixinConfig, String accessToken) {
        String key = jsApiTicketKey + "_" + weixinConfig.getAppId();
        String jsApiTicket = (String) cacheManager.get(key);
        if (StringUtil.isEmpty(jsApiTicket)) {
            String url = jsapiTicketUrl + "?access_token=" + accessToken + "&type=jsapi";
            String json = HttpsUtil.doGet(url);
            log.info("getJsApiTicket=>{}", json);
            WeixinOfficeJsApiTicket token = JSONUtil.toBean(json, WeixinOfficeJsApiTicket.class);
            if (token != null && StringUtil.isNotEmpty(token.getTicket())) {
                jsApiTicket = token.getTicket();
                cacheManager.set(key, jsApiTicket, token.getExpires_in() - 60);
            } else {
//                return this.getApiTicket(weixinConfig, accessToken);
            }
        }
        return jsApiTicket;
    }

    @Override
    public String getApiTicket(WeixinConfig weixinConfig) {
        String accessToken = getAccessToken(weixinConfig);
        return getApiTicket(weixinConfig, accessToken);
    }

    @Override
    public String signature(String nonceStr, String timestamp, String url, String jsApiTicket) {
        //签名生成规则如下：参与签名的字段包括noncestr（随机字符串）, 有效的jsapi_ticket, timestamp（时间戳）, url（当前网页的URL，不包含#及其后面部分
        String forSign = "jsapi_ticket=" + jsApiTicket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        return SecureUtil.sha1(forSign);
    }

}
