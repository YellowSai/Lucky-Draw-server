package cn.jzcscw.third.weixin.bean;

import lombok.Data;

@Data
public class WeixinPayH5UnifiedResponseData {
    private String code;
    private String message;
    private String detail;
    private String h5_url;
}
