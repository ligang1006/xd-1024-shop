package net.gaven.util;

import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;

/**
 * @author: lee
 * @create: 2021/8/5 8:52 上午
 **/
public class MD5Util {
    public static String MD5(String data) {
        try {
            java.security.MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }

            return sb.toString().toUpperCase();
        } catch (Exception exception) {
        }
        return null;
    }

    /**
     * 获取加盐的密码
     *
     * @param pwd
     * @param salt
     * @return
     */
    public static String getCryptPwd(String pwd, String salt) {
        if (StringUtils.isEmpty(pwd)) {
            return pwd;
        }
        //生成秘钥
        if (StringUtils.isEmpty(salt)) {
            salt = "$1$" + RandomUtil.getStringNumRandom(8);
        }
        //密码 + 加盐处理
        return Md5Crypt.md5Crypt(pwd.getBytes(), salt);
    }
}
