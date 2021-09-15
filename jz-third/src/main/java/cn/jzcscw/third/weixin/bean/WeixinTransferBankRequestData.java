package cn.jzcscw.third.weixin.bean;

import cn.hutool.core.util.RandomUtil;
import cn.jzcscw.third.weixin.config.WeixinPayConfig;
import cn.jzcscw.third.weixin.util.WeixinSignature;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Data
public class WeixinTransferBankRequestData {
    private String mchid = "";
    private String partner_trade_no = "";
    private String nonce_str = "";
    private String sign = "";

    private String enc_bank_no = "";
    private String enc_true_name = "";
    //收款方开户行 https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_4&index=5
    private String bank_code = "";
    private int amount = 0;
    private String desc = "";

    public WeixinTransferBankRequestData() {
    }

    public WeixinTransferBankRequestData(WeixinPayConfig weixinPayConfig, int amount, String partnerTradeNo, String name, String cardId, String bankCode, String desc) {
        this.mchid = weixinPayConfig.getMchId();
        this.partner_trade_no = partnerTradeNo;
        this.nonce_str = RandomUtil.randomString(30);
        this.sign = WeixinSignature.getSign(toMap(), weixinPayConfig.getApiKey());
        this.enc_bank_no = cardId;
        this.enc_true_name = name;
        this.bank_code = bankCode;
        this.amount = amount;
        this.desc = desc;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if (obj != null) {
                    map.put(field.getName(), obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
