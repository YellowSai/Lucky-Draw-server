package cn.jzcscw.third.weixin.bean;

import lombok.Data;

@Data
public class WeixinPayNotifyRequestData {
    private String id;
    private String create_time;
    private String event_type;
    private String resource_type;
    private Resource resource;
    private String summary;

    @Data
    public static class Resource {
        private String algorithm;
        private String ciphertext;
        private String associated_data;
        private String original_type;
        private String nonce;
    }
}
