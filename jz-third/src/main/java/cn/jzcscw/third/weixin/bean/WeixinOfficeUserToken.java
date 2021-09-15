package cn.jzcscw.third.weixin.bean;

import lombok.Data;

@Data
public class WeixinOfficeUserToken {
    private int errcode;
    private String errmsg;
    private String unionid;
    private String openid;
    private String access_token;
    private String refresh_token;
    private int expires_in;
    private String scope;
}
