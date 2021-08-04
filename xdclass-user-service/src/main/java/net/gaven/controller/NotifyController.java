package net.gaven.controller;

import com.google.code.kaptcha.Producer;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.gaven.service.ICaptchaService;
import net.gaven.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author: lee
 * @create: 2021/8/4 10:29 上午
 **/
@Slf4j
@RestController
@RequestMapping("/api/v1/notify")
public class NotifyController {

    //    @Autowired
//    private Producer captchaProduct;
    @Autowired
    private ICaptchaService captchaService;

    @ApiOperation("获取图形验证码")
    @RequestMapping("/captcha")
    private void captchaProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String text = captchaProduct.createText();
//        log.info("图形验证码" + text);
//        BufferedImage image = captchaProduct.createImage(text);
//        ServletOutputStream outputStream = null;
//        try {
//            outputStream = response.getOutputStream();
//            ImageIO.write(image, "jpg", outputStream);
//            outputStream.flush();
//            outputStream.close();
//        } catch (IOException e) {
//            log.error("创建图片错误{}" + e);
//        }
        captchaService.createCaptcha(response.getOutputStream());


    }
}
