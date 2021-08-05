package net.gaven.util;

import java.util.Random;

/**
 * 获取随机数
 *
 * @author: lee
 * @create: 2021/8/5 1:56 下午
 **/
public class RandomNumUtil {
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
}
