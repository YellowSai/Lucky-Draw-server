package cn.jzcscw.third.weixin.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class WeixinPayRefundNotifyData {
    private String mchid;
    private String out_trade_no;
    private String transaction_id;
    private String out_refund_no;
    private String refund_id;
    private String refund_status;
    private String success_time;
    private String user_received_account;

    private Amount amount;


    @Data
    @Accessors(chain = true)
    public static class Amount {
        private int total;
        private int refund;
        private int payer_total;
        private String payer_refund;
    }

}
