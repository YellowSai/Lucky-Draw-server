package cn.marsLottery.server.service;

public interface SmsService {

    void sendSmsCaptcha(String mobile);

    void sendSms(String content, String mobile,int threadSleepTime);

    String sendTestSmsCaptcha(String mobile);

    void checkCaptcha(String mobile,String captcha);
}
