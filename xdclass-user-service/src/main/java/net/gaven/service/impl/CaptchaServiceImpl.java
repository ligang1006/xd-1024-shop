package net.gaven.service.impl;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import net.gaven.enums.BizCodeEnum;
import net.gaven.enums.SendCodeEnum;
import net.gaven.service.ICaptchaService;
import net.gaven.service.INotifyService;
import net.gaven.util.HttpUtil;
import net.gaven.util.JsonData;
import net.gaven.util.MyRedisTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author: lee
 * @create: 2021/8/4 11:23 上午
 **/
@Slf4j
@Service
public class CaptchaServiceImpl implements ICaptchaService {
    @Resource
    private Producer captchaProduct;

    @Autowired
    private MyRedisTemplate redisTemplate;
    private static final long CAPTCHA_CODE_EXPIRED = 60 * 10L;
    @Autowired
    private INotifyService notifyService;

    /**
     * 1、创建要创建的文字
     * 2、创建图片
     * 3、获取输出流
     * 4、
     *
     * @param outputStream
     */
    @Override
    public void createCaptcha(ServletOutputStream outputStream, String cacheKey) {
        String text = captchaProduct.createText();
        BufferedImage image = captchaProduct.createImage(text);
        //加入缓存
        redisTemplate.set(cacheKey, text, CAPTCHA_CODE_EXPIRED, TimeUnit.SECONDS);

        log.info("创建图片验证码{}" + text);
        try {


            ImageIO.write(image, "jpg", outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error("创建图片验证码失败+{}" + e);
        }
    }

    /**
     * 校验图形验证码
     * @param captcha
     * @param cacheKey
     * @return
     */
    @Override
    public JsonData checkCaptcha(String captcha, String cacheKey, String to) {
        Object o = redisTemplate.get(cacheKey);
        String oldValue = null;
        if (o instanceof String) {
            oldValue = (String) o;
        }
        //进行校验，输入值与旧的缓存值进行比较
        if (oldValue != null
                && StringUtils.isNotEmpty(captcha)
                && oldValue.equalsIgnoreCase(captcha)) {
            //删除对应的key
            redisTemplate.delete(cacheKey);
            //发送邮箱
           return notifyService.sendCode(SendCodeEnum.USER_REGISTER, to);

        }
        return JsonData.buildResult(BizCodeEnum.CODE_CAPTCHA_ERROR);
    }
}
