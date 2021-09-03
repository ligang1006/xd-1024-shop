package net.gaven.util;

import java.util.Random;
import java.util.UUID;

/**
 * 获取随机数
 *
 * @author: lee
 * @create: 2021/8/5 1:56 下午
 **/
public class RandomUtil {

    public static String getRandomString() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 获取指定长度的0-9随机数
     *
     * @param length
     * @return
     */
    public static String getRandomNum(int length) {
        String num = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(num.charAt(random.nextInt(9)));
        }
        return sb.toString();
    }

    private static final String ALL_CHAR_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 随机生成字符串
     *
     * @param length
     * @return
     */
    public static String getStringNumRandom(int length) {
        //生成随机数字和字母,
        Random random = new Random();
        StringBuilder saltString = new StringBuilder(length);
        for (int i = 1; i <= length; ++i) {
            saltString.append(ALL_CHAR_NUM.charAt(random.nextInt(ALL_CHAR_NUM.length())));
        }
        return saltString.toString();
    }
}
