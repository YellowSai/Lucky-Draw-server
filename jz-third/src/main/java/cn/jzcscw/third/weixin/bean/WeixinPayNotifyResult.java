package cn.jzcscw.third.weixin.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WeixinPayNotifyResult {
    private String code;
    private String message;
}
