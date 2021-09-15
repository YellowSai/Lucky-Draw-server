package cn.marsLottery.server.web;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by lixue on 11/03/2018.
 */
@Component
public class FreemakerExceptionHandler implements TemplateExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(TemplateExceptionHandler.class);

    @Override
    public void handleTemplateException(TemplateException te, Environment env, Writer out) throws TemplateException {
        String missingVariable = "undefined";
        try {
            String[] tmp = te.getMessageWithoutStackTop().split("\n");
            if (tmp.length > 1) {
                tmp = tmp[1].split(" ");
            }
            if (tmp.length > 1) {
                missingVariable = tmp[1];
            }

            out.write("[!!!出错了，请联系网站管理员：${ " + missingVariable + "}]");
            logger.error("freemarker模板处理异常:", te);
        } catch (IOException e) {
            throw new TemplateException("Failed to print error message. Cause: " + e, env);
        }
    }
}
