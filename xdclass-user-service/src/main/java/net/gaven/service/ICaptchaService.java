package net.gaven.service;

import net.gaven.util.JsonData;

import javax.servlet.ServletOutputStream;

/**
 * 图形验证码
 *
 * @author: lee
 * @create: 2021/8/4 11:22 上午
 **/
public interface ICaptchaService {
    /**
     * 创建验证码
     *
     * @param outputStream
     * @param cacheKey     缓存Key
     */
    void createCaptcha(ServletOutputStream outputStream, String cacheKey);

    /**
     * 校验图形验证码
     * 通过req获取key，与之前redis缓存的key进行比较
     *
     * @param captcha
     * @param cacheKey
     * @return
     */
    JsonData checkCaptcha(String captcha, String cacheKey, String to);
}
