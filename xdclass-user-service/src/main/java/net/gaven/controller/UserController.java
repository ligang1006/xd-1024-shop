package net.gaven.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.gaven.enums.BizCodeEnum;
import net.gaven.service.ICaptchaService;
import net.gaven.service.IMailService;
import net.gaven.util.HttpUtil;
import net.gaven.util.JsonData;
import net.gaven.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static net.gaven.constant.Constant.REDIS_USER_SERVICE_PREFIX;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 二当家小D
 * @since 2021-08-03
 */
@Api("用户模块")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private ICaptchaService captchaService;

    /**
     * 与redis中缓存的Key进行对比，相同就成功
     *
     * @param to
     * @param captcha
     * @return
     */
    @ApiOperation("图形验证码校验")
    @GetMapping("/check_captcha")
    public JsonData checkCaptcha(@RequestParam(value = "to", required = true) String to,
                                 @RequestParam(value = "captcha") String captcha,
                                 HttpServletRequest request) {


        String ipAddr = HttpUtil.getIpAddr(request);
        String cacheKey = REDIS_USER_SERVICE_PREFIX + MD5Util.MD5(ipAddr + request.getHeader("User-Agent"));
        return captchaService.checkCaptcha(captcha, cacheKey, to);

    }
}

