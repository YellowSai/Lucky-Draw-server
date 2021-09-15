
package cn.jzcscw.third.sms.aliyun;

import lombok.Data;

import java.io.Serializable;

@Data
public class SmsTemplate implements Serializable {
    private String Code;
    private String CreateDate;
    private String Message;
    private String Reason;
    private String RequestId;
    private String TemplateCode;
    private String TemplateContent;
    private String TemplateName;
    private Long TemplateStatus;
    private Long TemplateType;

}
