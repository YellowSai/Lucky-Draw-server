package cn.jzcscw.third.weixin.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class WeixinPayRefundRequestData {
    // 每个字段具体的意思请查看API文档
    //一下2个参数二选一传递即可
    private String transaction_id = "";
    private String out_trade_no = "";

    private String out_refund_no = "";
    private String reason = "";
    private String notify_url = "";
    private String funds_account = "";
    private Amount amount;
    //指定商品退款需要传此参数，其他场景无需传递。
    private GoodsDetail goods_detail;

    public WeixinPayRefundRequestData(String transactionId, String outTradeNo, String outRefundNo, String reason,
                                      String notifyUrl, String fundsAccount, int refundFee,
                                      int totalFee, String currency) {
        this.transaction_id = transactionId;
        this.out_trade_no = outTradeNo;
        this.out_refund_no = outRefundNo;
        this.reason = reason;
        this.funds_account = fundsAccount;
        this.notify_url = notifyUrl;
        this.amount = new WeixinPayRefundRequestData.Amount().setRefund(refundFee).setTotal(totalFee).setCurrency(currency);
    }

    @Data
    @Accessors(chain = true)
    public static class Amount {
        private int refund;
        private int total;
        private String currency;
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
