package cn.jzcscw.third.weixin.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class WeixinPayRefundResponseData {
    private String code;
    private String message;
    private String refund_id;
    private String out_refund_no;
    private String transaction_id;
    private String out_trade_no;
    private String channel;
    private String user_received_account;
    private String success_time;
    private String create_time;
    private String status;
    private String funds_account;
    private Amount amount;
    private PromotionDetail promotion_detail;

    @Data
    @Accessors(chain = true)
    public static class Amount {
        private int refund;
        private int total;
        private int payer_total;
        private int payer_refund;
        private int settlement_refund;
        private int settlement_total;
        private int discount_refund;
        private String currency;
    }

    @Data
    @Accessors(chain = true)
    public static class PromotionDetail {
        private String promotion_id;
        private String scope;
        private String type;
        private int amount;
        private int refund_amount;
        private GoodsDetail goods_detail;
    }

    @Data
    @Accessors(chain = true)
    public static class GoodsDetail {
        private String merchant_goods_id;
        private String wechatpay_goods_id;
        private String goods_name;
        private int quantity;
        private int unit_price;
        private int refund_amount;
        private int refund_quantity;
    }
}
