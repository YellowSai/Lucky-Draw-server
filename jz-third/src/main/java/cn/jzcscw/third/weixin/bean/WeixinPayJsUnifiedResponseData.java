package cn.jzcscw.third.weixin.bean;

import lombok.Data;

@Data
public class WeixinPayJsUnifiedResponseData {
    private String code;
    private String message;
    private String detail;
    private String prepay_id;
}
