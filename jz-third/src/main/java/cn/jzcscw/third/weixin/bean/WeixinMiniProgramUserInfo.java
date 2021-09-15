package cn.jzcscw.third.weixin.bean;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.util.Map;

@Data
public class WeixinMiniProgramUserInfo {
    private String openid;
    private String unionid;
    private String nickname;
    private String sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private Watermark watermark;

    public WeixinMiniProgramUserInfo() {
    }

    public WeixinMiniProgramUserInfo(Map result) {
        if (CollectionUtil.isNotEmpty(result)) {
            openid = (String) result.get("openId");
            unionid = (String) result.get("unionId");
            nickname = (String) result.get("nickName");
            sex = (String) result.get("openId");
            language = (String) result.get("language");
            city = (String) result.get("city");
            province = (String) result.get("province");
            country = (String) result.get("country");
            headimgurl = (String) result.get("avatarUrl");
            watermark = JSONUtil.toBean((JSONObject) result.get("watermark"), Watermark.class);
        }
    }

    @Data
    private class Watermark {
        private long timestamp;
        private String appid;
    }
}

