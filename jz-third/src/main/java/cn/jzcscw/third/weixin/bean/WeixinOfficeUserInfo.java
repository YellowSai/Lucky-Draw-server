package cn.jzcscw.third.weixin.bean;

import lombok.Data;

@Data
public class WeixinOfficeUserInfo {
    private String unionid;
    private String openid;
    private String nickname;
    private String sex;        // 1男2 女
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private String privilege;    // 用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
}
