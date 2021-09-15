package cn.jzcscw.third.sms.aliyun;

import lombok.Data;

@Data
public class SmsSendResult {
    private String RequestId;
    private String Message;
    private String Code;
    private String BixId;
}
