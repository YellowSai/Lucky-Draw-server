package cn.jzcscw.third.weixin.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import cn.jzcscw.commons.exception.JzRuntimeException;
import cn.jzcscw.commons.util.HttpsUtil;
import cn.jzcscw.commons.util.StringUtil;
import cn.jzcscw.commons.web.ResponseUtil;
import cn.jzcscw.third.weixin.bean.WeixinOfficeUserInfo;
import cn.jzcscw.third.weixin.bean.WeixinOfficeUserToken;
import cn.jzcscw.third.weixin.bean.WeixinTemplateRequestData;
import cn.jzcscw.third.weixin.bean.WeixinTemplateResult;
import cn.jzcscw.third.weixin.config.WeixinConfig;
import cn.jzcscw.third.weixin.service.WeixinOfficeService;
import cn.jzcscw.third.weixin.service.WeixinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@Slf4j
@Service
public class WeixinOfficeServiceImpl implements WeixinOfficeService {

    private static String codeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize";

    private static String userAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";

    private static String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo";

    private static String sendMessageUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    @Autowired
    WeixinService weixinService;

    @Override
    public void redirectForCode(WeixinConfig weixinConfig, HttpServletResponse response, String redirectUri, String scope, String state) {
        String url = null;
        try {
            url = codeUrl + "?appid=" + weixinConfig.getAppId() + "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8") + "&response_type=code&scope=" + scope + "&state=" + state + "#wechat_redirect";
            ResponseUtil.sendRedirect(response, url);
        } catch (UnsupportedEncodingException e) {
            log.error("getCode error {}", e);
        }
    }

    @Override
    public WeixinOfficeUserToken getUserAccessToken(WeixinConfig weixinConfig, String code) {
        String url = userAccessTokenUrl + "?appid=" + weixinConfig.getAppId() + "&secret=" + weixinConfig.getAppSecret() + "&code=" + code + "&grant_type=authorization_code";
        String json = HttpsUtil.doGet(url);
        log.info("getUserAccessToken get {}", json);
        WeixinOfficeUserToken accessToken = JSONUtil.toBean(json, WeixinOfficeUserToken.class);
        if (accessToken == null || accessToken.getErrcode() > 0) {
            throw new JzRuntimeException("微信登录已失效，请重新登录");
        }
        return accessToken;
    }

    @Override
    public WeixinOfficeUserInfo getUserInfo(String accessToken, String openId) {
        String url = userInfoUrl + "?access_token=" + accessToken + "&openid=" + openId;
        String json = HttpsUtil.doGet(url);
        log.info("getUserInfo {}", json);
        WeixinOfficeUserInfo userInfo = JSONUtil.toBean(json, WeixinOfficeUserInfo.class);
        if (StringUtil.isEmpty(userInfo.getOpenid()) && StringUtil.isEmpty(userInfo.getUnionid())) {
            throw new JzRuntimeException("微信登录用户信息获取失败");
        }
        return userInfo;
    }

    @Override
    public WeixinOfficeUserInfo getUserInfo(WeixinConfig weixinConfig, String code) {
        WeixinOfficeUserToken weixinOfficeUserToken = this.getUserAccessToken(weixinConfig, code);
        return getUserInfo(weixinOfficeUserToken.getAccess_token(), weixinOfficeUserToken.getOpenid());
    }

    @Override
    public String signature(String nonceStr, String timestamp, String url, String jsApiTicket) {
        //签名生成规则如下：参与签名的字段包括noncestr（随机字符串）, 有效的jsapi_ticket, timestamp（时间戳）, url（当前网页的URL，不包含#及其后面部分
        String forSign = "jsapi_ticket=" + jsApiTicket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        return SecureUtil.sha1(forSign);
    }

    @Override
    public WeixinTemplateResult sendTemplateMessage(WeixinConfig weixinConfig, WeixinTemplateRequestData weixinTemplateRequestData) {
        String accessToken = weixinService.getAccessToken(weixinConfig);
        sendMessageUrl = sendMessageUrl.replaceAll("ACCESS_TOKEN", accessToken);

        String dataStr = JSONUtil.toJsonStr(weixinTemplateRequestData);
        String jsonStr = HttpsUtil.doPostJsonStr(sendMessageUrl, dataStr);
        try {
            WeixinTemplateResult weixinTemplateResult = JSONUtil.toBean(jsonStr, WeixinTemplateResult.class);
            return weixinTemplateResult;
        } catch (Exception e) {
            log.error("send weixin Template error{}", e);
        }
        return null;
    }

    @Override
    public WeixinTemplateRequestData getWeixinTemplateRequestData(String miniProgramAppId, String templateId, String openId, String url, String pagepath, Map<String, WeixinTemplateRequestData.TemplateData> data) {
        return new WeixinTemplateRequestData(miniProgramAppId, templateId, openId, url, pagepath, data);
    }

}
