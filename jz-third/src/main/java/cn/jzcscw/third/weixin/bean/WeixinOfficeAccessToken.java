package cn.jzcscw.third.weixin.bean;

import lombok.Data;

@Data
public class WeixinOfficeAccessToken {
    private int errcode;
    private String errmsg;
    private String access_token;
    private int expires_in;
    private int openid;
    private int snsapi_base;
}
