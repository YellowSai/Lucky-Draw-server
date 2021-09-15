package cn.jzcscw.third.weixin.bean;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
public class WeixinPayH5UnifiedResquestData {
    // 每个字段具体的意思请查看API文档
    private String appid = "";
    private String mchid = "";
    private String description = "";
    private String out_trade_no = "";
    private String time_expire = "";
    private String attach = "";
    private String notify_url = "";
    private String goods_tag = "";
    private Amount amount;
    private Detail detail;
    private SceneInfo scene_info;
    private SettleInfo settle_info;

    public WeixinPayH5UnifiedResquestData(String appId, String mchId, String description, String outTradeNo, String timeExpire,
                                          String attach, String notifyUrl, String goodsTag,
                                          int totalFee, String currency, String clientIp,
                                          List<GoodsDetail> goodsDetail) {

        this.appid = appId;
        this.mchid = mchId;
        this.description = description;
        this.out_trade_no = outTradeNo;
        this.time_expire = timeExpire;
        this.attach = attach;
        this.notify_url = notifyUrl;
        this.goods_tag = goodsTag;
        this.amount = new Amount().setTotal(totalFee).setCurrency(currency);
        this.detail = new Detail().setCost_price(totalFee).setGoods_detail(goodsDetail);
        this.scene_info = new SceneInfo().setPayer_client_ip(clientIp);
    }


    @Data
    @Accessors(chain = true)
    public static class Amount {
        private int total;
        private String currency;
    }

    @Data
    @Accessors(chain = true)
    public static class Detail {
        private int cost_price;
        private String invoice_id;
        private List<GoodsDetail> goods_detail;
    }

    @Data
    @Accessors(chain = true)
    public static class GoodsDetail {
        private String merchant_goods_id;
        private String wechatpay_goods_id;
        private String goods_name;
        private int quantity;
        private int unit_price;
    }

    @Data
    @Accessors(chain = true)
    public static class SceneInfo {
        private String payer_client_ip;
        private String device_id;
        private StoreInfo storeInfo;
    }

    @Data
    @Accessors(chain = true)
    public static class StoreInfo {
        private String id;
        private String name;
        private String area_code;
        private String address;
    }

    @Data
    @Accessors(chain = true)
    public static class SettleInfo {
        private boolean profit_sharing;
    }

}
