package cn.jzcscw.third.weixin.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class WeixinConfig implements Serializable {
    /**
     * 名称
     */
    private String name;
    /**
     * appid
     */
    private String appId;
    /**
     * appSecret
     */
    private String appSecret;
}
