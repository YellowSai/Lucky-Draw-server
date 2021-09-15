package cn.jzcscw.third.weixin.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
public class WeixinPayNotifyData {
    private String appid;
    private String mchid;
    private String out_trade_no;
    private String transaction_id;
    private String trade_type;
    private String trade_state;
    private String trade_state_desc;
    private String bank_type;
    private String attach;
    private String success_time;

    private Payer payer;
    private Amount amount;
    private SceneInfo scene_info;
    private String promotion_detail;

    public WeixinPayNotifyData() {
    }

    public WeixinPayNotifyData(Map<String, Object> respData) {
        appid = (String) respData.get("appid");
        mchid = (String) respData.get("mch_id");
        out_trade_no = (String) respData.get("out_trade_no");
        transaction_id = (String) respData.get("transaction_id");
        trade_type = (String) respData.get("trade_type");
        bank_type = (String) respData.get("bank_type");
        attach = (String) respData.get("attach");
        success_time = (String) respData.get("time_end");
        payer.openid = (String) respData.get("openid");

        amount.currency = (String) respData.get("fee_type");

        amount.total = (int) respData.get("total_fee");
        amount.payer_total = (int) respData.get("settlement_total_fee");
        amount.payer_currency = (String) respData.get("fee_type");

        scene_info.device_id = (String) respData.get("device_info");
    }

    @Data
    @Accessors(chain = true)
    public static class Payer {
        private String openid;
    }

    @Data
    @Accessors(chain = true)
    public static class Amount {
        private int total;
        private String currency;
        private int payer_total;
        private String payer_currency;
    }

    @Data
    @Accessors(chain = true)
    public static class SceneInfo {
        private String device_id;
    }

    @Data
    @Accessors(chain = true)
    public static class PromotionData {
        private String coupon_id;
        private String name;
        private String scope;
        private String type;
        private int amount;
        private String stock_id;
        private int wechatpay_contribute;
        private int merchant_contribute;
        private int other_contribute;
        private String currency;
        List<GoodsDetail> goods_detail;
    }

    @Data
    @Accessors(chain = true)
    public static class GoodsDetail {
        private String goods_id;
        private int quantity;
        private int unit_price;
        private int discount_amount;
        private String goods_remark;
    }
}
