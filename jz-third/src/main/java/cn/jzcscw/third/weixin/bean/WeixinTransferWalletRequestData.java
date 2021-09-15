package cn.jzcscw.third.weixin.bean;

import cn.hutool.core.util.RandomUtil;
import cn.jzcscw.third.weixin.config.WeixinPayConfig;
import cn.jzcscw.third.weixin.util.WeixinSignature;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Data
public class WeixinTransferWalletRequestData {
    //申请商户号的appid或商户号绑定的appid
    private String mch_appid = "";
    private String mchid = "";
    private String device_info = "";
    private String nonce_str = "";
    private String sign = "";
    private String partner_trade_no = "";
    private String openid = "";
    //校验用户姓名选项
    // NO_CHECK：不校验真实姓名
    //FORCE_CHECK：强校验真实姓名
    private String check_name = "";
    private String re_user_name = "";
    private int amount = 0;
    private String desc = "";
    private String spbill_create_ip = "";

    public WeixinTransferWalletRequestData() {
    }

    public WeixinTransferWalletRequestData(WeixinPayConfig weixinPayConfig, String partnerTradeNo, String openid, String reUserName, int amount, String desc) {
        this.mch_appid = weixinPayConfig.getAppId();
        this.mchid = weixinPayConfig.getMchId();
        this.nonce_str = RandomUtil.randomString(30);
        this.sign = WeixinSignature.getSign(toMap(), weixinPayConfig.getApiKey());
        this.partner_trade_no = partnerTradeNo;
        this.openid = openid;
        this.check_name = "FORCE_CHECK";
        this.re_user_name = reUserName;
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
