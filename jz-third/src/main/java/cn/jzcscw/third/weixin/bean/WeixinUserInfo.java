package cn.jzcscw.third.weixin.bean;

import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author jianzheng
 */
@Data
public class WeixinUserInfo {
    private String openid;
    private String unionid;
    private String nickname;
    private String sex;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String privilege;

    public WeixinUserInfo(WeixinOfficeUserInfo weixinOfficeUserInfo) {
        if (weixinOfficeUserInfo != null) {
            BeanUtils.copyProperties(weixinOfficeUserInfo, this);
        }
    }

    public WeixinUserInfo(WeixinMiniProgramUserInfo weixinMiniProgramUserInfo) {
        if (weixinMiniProgramUserInfo != null) {
            BeanUtils.copyProperties(weixinMiniProgramUserInfo, this);
        }
    }

    public WeixinUserInfo() {
    }
}

