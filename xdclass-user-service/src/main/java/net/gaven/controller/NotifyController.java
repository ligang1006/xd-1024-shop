package net.gaven.controller;

import com.google.code.kaptcha.Producer;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.gaven.service.ICaptchaService;
import net.gaven.service.IMailService;
import net.gaven.util.HttpUtil;
import net.gaven.util.JsonData;
import net.gaven.util.MD5Util;
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

    @Autowired
    private IMailService mailService;

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
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "create_date-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        String ipAddr = HttpUtil.getIpAddr(request);
        captchaService.createCaptcha(response.getOutputStream(), "user-service:captcha" + MD5Util.MD5(ipAddr + request.getHeader("User-Agent")));


    }


    @GetMapping("/send_email")
    public JsonData sentEmail(String to, String subject, String content) {
        mailService.sendMsg(to, subject, content);
        return JsonData.buildSuccess();
    }
}
