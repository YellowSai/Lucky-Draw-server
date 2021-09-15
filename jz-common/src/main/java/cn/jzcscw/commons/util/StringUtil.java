package cn.jzcscw.commons.util;

import cn.hutool.core.util.StrUtil;
import cn.jzcscw.commons.constant.Charsets;
import cn.jzcscw.commons.constant.Consts;

/**
 * 字符串处理工具类<br>
 * 扩展了hutool的StrUtil
 */
public class StringUtil extends StrUtil {

    /**
     * 字符串拼接
     *
     * @param args
     * @return
     */
    public static String concat(final Object... args) {
        StringBuilder str = new StringBuilder();
        for (Object arg : args) {
            str.append(String.valueOf(arg));
        }
        return str.toString();
    }

    /**
     * urlencode
     *
     * @param str
     * @param coding
     * @return
     */
    public static String encode(final String str, final String coding) {
        if (isEmpty(str)) {
            return Consts.EMPTY;
        }
        try {
            return java.net.URLEncoder.encode(str, coding);
        } catch (Exception e) {
            return str;
        }
    }

    /**
     * utf8 urlencode
     *
     * @param str
     * @return
     */
    public static String encode(final String str) {
        return encode(str, Charsets.UTF_8);
    }

    /**
     * urldecode
     *
     * @param str
     * @param coding
     * @return
     */
    public static String decode(final String str, final String coding) {
        if (isEmpty(str)) {
            return Consts.EMPTY;
        }
        try {
            return java.net.URLDecoder.decode(str, coding);
        } catch (Exception e) {
            return str;
        }
    }

    /**
     * utf8 urldecode
     *
     * @param str
     * @return
     */
    public static String decode(final String str) {
        return decode(str, Charsets.UTF_8);
    }

}
