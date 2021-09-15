package cn.marsLottery.server.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.jzcscw.commons.cache.CacheManager;
import cn.jzcscw.commons.exception.JzRuntimeException;
import cn.jzcscw.commons.util.HttpsUtil;
import cn.marsLottery.server.service.SmsService;
import cn.marsLottery.server.util.SecretUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author auto
 */
@Slf4j
@Service("SmsService")
public class SmsServiceImpl implements SmsService {
    private static final String sendtype = "验证码";
    private static final String supplie = "3";
    private static final String porjectId = "1596";
    private static final String isencode = "1";


    private static final int RETRY_TIME = 60;
    private static final int CAPTCHA_TIME = 60 * 5;
    private static final String RETRY_KEY = "retry";
    private static final String CAPTCHA_KEY = "captcha";

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void sendSmsCaptcha(String mobile) {
        Object retry = cacheManager.get(RETRY_KEY + mobile);
        if (retry != null) {
            throw new JzRuntimeException("验证码获取频繁，请稍后重试");
        }
        String captcha = RandomUtil.randomNumbers(4);
        String conten = "【玛氏箭牌】感谢您参加专属经销商百万俱乐部活动！您的验证码为" + captcha + "。5分钟内输入有效。疑问请咨询：400-012-8206 （接听时间：10:00-18:00）";
        this.sendSms(conten,mobile,0);

        cacheManager.set(RETRY_KEY + mobile, mobile, RETRY_TIME);
        cacheManager.set(CAPTCHA_KEY + mobile, captcha, CAPTCHA_TIME);
        log.info("验证码：" + captcha);
    }

    @Async
    @Override
    public void sendSms(String conten, String mobile,int threadSleepTime) {
        try {
            Thread.sleep(threadSleepTime);
            String timestamp = String.valueOf(DateUtil.date().getTime());
            timestamp = timestamp.substring(0, timestamp.length() - 3);

            String contenSign = URLEncoder.encode(conten, "utf8");
            StringBuilder lowCaseContenSign = new StringBuilder();
            for (int i = 0 , length = contenSign.length(); i < length;i++){
                if (contenSign.charAt(i) == '%'){
                    String lowCase = ("%" + contenSign.charAt(i + 1) + contenSign.charAt(i + 2)).toLowerCase();
                    lowCaseContenSign.append(lowCase);
                    i = i + 2;
                    continue;
                }
                lowCaseContenSign.append(contenSign.charAt(i));
            }

            String signString = "conten=" + lowCaseContenSign
                    + "&mobile=" + mobile
                    + "&porjectId=" + porjectId +
                    "&secret=27eRZakvAwED"
                    + "&sendtype=" + sendtype
                    + "&supplie=" + supplie
                    + "&timestamp=" + timestamp;

            String url = "http://MessageApi.iseedling.com/Controller/SMSController.ashx";
            Map<String, String> param = new HashMap<>();
            param.put("sign", SecretUtil.MD5(signString).toUpperCase());
            param.put("conten", conten);
            param.put("mobile", mobile);
            param.put("sendtype", sendtype);
            param.put("supplie", supplie);
            param.put("porjectId", porjectId);
            param.put("timestamp", timestamp);
            param.put("isencode", isencode);

            String json = HttpsUtil.doPost(url, param);
            log.debug("短信接口：{}", json);
        } catch (Exception e) {
            throw new JzRuntimeException(e.getMessage());
        }
    }

    @Override
    public String sendTestSmsCaptcha(String mobile) {
        Object retry = cacheManager.get(RETRY_KEY + mobile);
        if (retry != null) {
            throw new JzRuntimeException("验证码获取频繁，请稍后重试");
        }
        String captcha = RandomUtil.randomNumbers(4);
        cacheManager.set(RETRY_KEY + mobile, mobile, RETRY_TIME);
        cacheManager.set(CAPTCHA_KEY + mobile, captcha, CAPTCHA_TIME);
        log.info("验证码：" + captcha);
        return captcha;
    }

    @Override
    public void checkCaptcha(String mobile, String captcha) {
        Object backups = cacheManager.get(CAPTCHA_KEY + mobile);
        if (backups == null) {
            throw new JzRuntimeException("验证码已过期");
        }
        if (!String.valueOf(backups).equals(captcha)) {
            throw new JzRuntimeException("验证码错误");
        }
    }
}
