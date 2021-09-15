package cn.jzcscw.third.weixin.config;

import cn.jzcscw.third.weixin.bean.WeixinTemplateMessage;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jianzheng
 */
@Data
@Component
@ConfigurationProperties("weixin.message")
public class WeixinTemplateMessageConfig {

    private List<WeixinTemplateMessage> templateList;

    public WeixinTemplateMessage getTemplateByName(String name) {
        return templateList.stream().filter(t -> t.getTemplateName().equals(name)).findFirst().get();
    }
}
