package cn.marsLottery.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecretUtil {
    public static String MD5 (String secretString) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(secretString.getBytes());
        byte[] bytes = md.digest();
        StringBuilder stringBuffer = new StringBuilder();
        for (byte b : bytes) {
            int bt = b & 0xff;
            if (bt < 16) {
                stringBuffer.append(0);
            }
            stringBuffer.append(Integer.toHexString(bt));
        }
        return stringBuffer.toString();
    }
}
