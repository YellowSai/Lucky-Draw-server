package cn.jzcscw.third.sms;

import cn.jzcscw.third.sms.aliyun.SmsTemplate;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


@Data
@Component
@ConfigurationProperties("aliyun.sms")
public class AliyunConfig {

    private String accessKeyID;

    private String accessKeySecret;

    private String sign;

    private List<SmsTemplate> templateList;

    public SmsTemplate getTemplateByName(String name) {
        return templateList.stream().filter(t -> t.getTemplateName().equals(name)).findFirst().get();
    }
}
