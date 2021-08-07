package net.gaven.util;

import java.util.UUID;

/**
 * @author: lee
 * @create: 2021/8/7 10:22 上午
 **/
public class CommonUtil {
    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "").substring(0, 32);
    }
}
