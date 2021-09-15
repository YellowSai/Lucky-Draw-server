package cn.jzcscw.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略身份验证
 * 如果
 * !!!注意使用此注解后,WebContext获取的userId可能等于0，即未登录
 */

@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiAuth {
    boolean ignore();
}
