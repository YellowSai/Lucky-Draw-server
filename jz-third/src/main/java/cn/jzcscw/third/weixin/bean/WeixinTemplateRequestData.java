package cn.jzcscw.third.weixin.bean;

import lombok.Data;

import java.util.Map;

/**
 * @author jianzheng
 */
@Data
public class WeixinTemplateRequestData {

    /**
     * 接收者openid
     */
    private String touser;

    /**
     * 模板ID
     */
    private String template_id;

    /**
     * 模板跳转链接（海外帐号没有跳转能力）
     * url 和 miniprogram 都是非必填字段，若都不传则模板无跳转；若都传，会优先跳转至小程序。
     */
    private String url;

    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     * url 和 miniprogram 都是非必填字段，若都不传则模板无跳转；若都传，会优先跳转至小程序。
     */
    private Miniprogram miniprogram;

    /**
     * 模板数据
     */
    private Map<String, TemplateData> data;

    public WeixinTemplateRequestData() {
    }

    public WeixinTemplateRequestData(String appId, String templateId, String openId, String url, String pagepath, Map<String, TemplateData> data) {
        this.setTouser(openId);
        this.setTemplate_id(templateId);
        this.setUrl(url);
        this.setMiniprogram(new Miniprogram(appId, pagepath));
        this.setData(data);
    }

    @Data
    private class Miniprogram {

        /**
         * 所需跳转到的小程序appid
         */
        private String appid;

        /**
         * 所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar）
         */
        private String pagepath;

        public Miniprogram() {
        }

        public Miniprogram(String appId, String pagepath) {
            this.setAppid(appId);
            this.setPagepath(pagepath);
        }
    }

    @Data
    public static class TemplateData {

        /**
         * 值
         */
        private String value;

        /**
         * 模板内容字体颜色，不填默认为黑色
         */
        private String color;

        public TemplateData() {
        }

        public TemplateData(String value) {
            this.setValue(value);
        }

        public TemplateData(String value, String color) {
            this.setValue(value);
            this.setColor(color);
        }
    }
}

