package net.gaven.service.impl;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import net.gaven.service.ICaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author: lee
 * @create: 2021/8/4 11:23 上午
 **/
@Slf4j
@Service
public class CaptchaService implements ICaptchaService {
    @Autowired
    private Producer captchaProduct;

    /**
     * 1、创建要创建的文字
     * 2、创建图片
     * 3、获取输出流
     * 4、
     *
     * @param outputStream
     */
    @Override
    public void createCaptcha(ServletOutputStream outputStream) {
        String text = captchaProduct.createText();
        BufferedImage image = captchaProduct.createImage(text);
        log.info("创建图片验证码{}" + text);
        try {
            ImageIO.write(image, "jpg", outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error("创建图片验证码失败+{}" + e);
        }
    }
}
