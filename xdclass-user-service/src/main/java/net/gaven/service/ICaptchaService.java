package net.gaven.service;

import javax.servlet.ServletOutputStream;

/**
 * 图形验证码
 *
 * @author: lee
 * @create: 2021/8/4 11:22 上午
 **/
public interface ICaptchaService {
    /**
     *
     * @param outputStream
     */
    public void createCaptcha(ServletOutputStream outputStream,String cacheKey);
}
