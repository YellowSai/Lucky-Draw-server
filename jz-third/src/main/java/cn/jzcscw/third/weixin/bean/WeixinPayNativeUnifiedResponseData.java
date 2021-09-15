package cn.jzcscw.third.weixin.bean;

import lombok.Data;

@Data
public class WeixinPayNativeUnifiedResponseData {
    private String code;
    private String message;
    private String code_url;
}
