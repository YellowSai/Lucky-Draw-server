package cn.jzcscw.third.weixin.bean;

import lombok.Data;

@Data
public class WeixinOfficeJsApiTicket {
    private int errcode;
    private String errmsg;
    private String ticket;
    private int expires_in;
}
