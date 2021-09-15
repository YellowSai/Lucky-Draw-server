package cn.jzcscw.third.weixin.bean;

import lombok.Data;

/**
 * @author jianzheng
 */
@Data
public class WeixinTemplateResult {

    /**
     * 成功返回：0
     */
    private int errcode;

    /**
     * 成功返回：ok
     */
    private String errmsg;

    private int msgid;
}
