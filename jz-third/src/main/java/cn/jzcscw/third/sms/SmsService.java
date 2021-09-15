package cn.jzcscw.third.sms;

import cn.jzcscw.third.sms.aliyun.SmsTemplate;

import java.util.Map;

public interface SmsService {

    boolean sendSms(String mobile, String signName, String templateId, Map<String, String> templateParam);

    SmsTemplate getSmsTemplate(String templateId);

    String getSmsContent(String signName, String templateContent, Map<String, String> templateParam);

    String sendSmsCaptcha(String mobile);
}
