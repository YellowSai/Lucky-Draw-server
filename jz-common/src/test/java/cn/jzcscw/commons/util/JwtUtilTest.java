package cn.jzcscw.commons.util;

import cn.hutool.core.util.RandomUtil;
import org.junit.Test;


public class JwtUtilTest {
    @Test
    public void generateSecret() {
        String randomStr = RandomUtil.randomString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 64);
        System.out.println("secret=>" + randomStr);
    }
}
