package cn.jzcscw.third.sms.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import cn.jzcscw.commons.cache.CacheManager;
import cn.jzcscw.commons.exception.JzRuntimeException;
import cn.jzcscw.third.sms.AliyunConfig;
import cn.jzcscw.third.sms.SmsService;
import cn.jzcscw.third.sms.aliyun.SmsSendResult;
import cn.jzcscw.third.sms.aliyun.SmsTemplate;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lixue
 */
@Slf4j
@Service("aliyunSmsService")
public class AliyunSmsServiceImpl implements SmsService {

    private static final int RETRY_TIME = 60;
    private static final int CAPTCHA_TIME = 60 * 30;
    private static final String RETRY_KEY = "retry";
    private static final String CAPTCHA_KEY = "captcha";

    @Autowired
    private AliyunConfig aliyunConfig;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public boolean sendSms(String mobile, String signName, String templateId, Map<String, String> templateParam) {
//        if ("dev".equals(appConfig.getEnv())) {
//            log.info("fake send sms mobile:{},signName:{},templateId:{},templateParam:{}", mobile, signName, templateId, templateParam);
//            return false;
//        }

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyunConfig.getAccessKeyID(), aliyunConfig.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");

        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateId);
        request.putQueryParameter("TemplateParam", JSONUtil.toJsonStr(templateParam));
        try {
            CommonResponse response = client.getCommonResponse(request);
            SmsSendResult smsSendResult = JSONUtil.toBean(response.getData(), SmsSendResult.class);
            log.debug("sendSms response => {}", smsSendResult);
            if ("OK".equals(smsSendResult.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (ClientException e) {
            log.error("sendSms error =>", e);
        }
        return true;
    }

    @Override
    public SmsTemplate getSmsTemplate(String templateId) {
        SmsTemplate smsTemplate = null;
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyunConfig.getAccessKeyID(), aliyunConfig.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("QuerySmsTemplate");
        request.putQueryParameter("RegionId", "cn-hangzhou");

        request.putQueryParameter("TemplateCode", templateId);
        try {
            CommonResponse response = client.getCommonResponse(request);
            smsTemplate = JSONUtil.toBean(response.getData(), SmsTemplate.class);
        } catch (ClientException e) {
            log.error("sendSms error =>", e);
        }
        return smsTemplate;
    }

    @Override
    public String getSmsContent(String signName, String templateContent, Map<String, String> templateParam) {
        String content = "【" + signName + "】" + templateContent;
        for (Map.Entry<String, String> entry : templateParam.entrySet()) {
            content = content.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return content;
    }

    @Override
    public String sendSmsCaptcha(String mobile) {
        Object retry = cacheManager.get(RETRY_KEY + mobile);
        if (retry != null) {
            throw new JzRuntimeException("验证码获取频繁，请稍后重试");
        }
        String captcha = RandomUtil.randomNumbers(4);
        cacheManager.set(RETRY_KEY + mobile, mobile, RETRY_TIME);
        cacheManager.set(CAPTCHA_KEY + mobile, captcha, CAPTCHA_TIME);

        log.info("验证码：" + captcha);
        SmsTemplate template = aliyunConfig.getTemplateByName("通用验证码");
        Map<String, String> templateParam = new HashMap<>();
        templateParam.put("code", captcha);
        this.sendSms(mobile, aliyunConfig.getSign(), template.getTemplateCode(), templateParam);

        return captcha;
    }

}
