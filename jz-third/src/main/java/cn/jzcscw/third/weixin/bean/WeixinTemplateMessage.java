
package cn.jzcscw.third.weixin.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jianzheng
 */
@Data
public class WeixinTemplateMessage implements Serializable {
    private String TemplateCode;
    private String TemplateName;
    private String TemplateContent;
}
