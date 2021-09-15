package cn.marsLottery.server.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class PasswordUtil {

    static public String createSalt() {
        return RandomUtil.randomString(32);
    }

    static public String encodePassword(String salt, String password) {
        String salted = mergePasswordAndSalt(salt, password);
        byte[] digest = sha256(salted);
        byte[] saltByteArr = salted.getBytes();

        for (int i = 1; i < 5000; i++) {
            digest = ArrayUtil.addAll(digest, saltByteArr);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(digest);
            digest = sha256(byteArrayInputStream);
        }
        return Base64.encode(digest);
    }

    private static String mergePasswordAndSalt(String salt, String password) {
        return password + "{" + salt + "}";
    }

    private static byte[] sha256(String str) {
        String digest = SecureUtil.sha256(str);
        return HexUtil.decodeHex(digest);
    }

    private static byte[] sha256(InputStream in) {
        String digest = SecureUtil.sha256(in);
        return HexUtil.decodeHex(digest);
    }
}
