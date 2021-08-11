package net.gaven.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.gaven.enums.BizCodeEnum;
import net.gaven.model.UserDO;
import net.gaven.request.UserLoginRequest;
import net.gaven.request.UserRegisterRequest;
import net.gaven.service.ICaptchaService;
import net.gaven.service.IFileService;
import net.gaven.service.IMailService;
import net.gaven.service.IUserService;
import net.gaven.util.HttpUtil;
import net.gaven.util.JsonData;
import net.gaven.util.MD5Util;
import net.gaven.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private IFileService fileService;
    @Autowired
    private IUserService userService;

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

    /**
     * 头像上传
     * 最大1M
     *
     * @param file
     * @return
     */
    @ApiOperation("上传头像")
    @PostMapping("/image_upload")
    public JsonData uploadUserImage(
            @ApiParam(value = "file", required = true)
            @RequestPart("file") MultipartFile file) {
        String userImageUrl = fileService.uploadUserImage(file);
        return StringUtils.isNotEmpty(userImageUrl) ? JsonData.buildSuccess(userImageUrl) : JsonData.buildResult(BizCodeEnum.FILE_UPLOAD_USER_IMG_FAIL);
    }

    @ApiOperation("用户注册")
    @PostMapping("register")
    public JsonData registerUser(
            @ApiParam("用户注册参数")
            @RequestBody UserRegisterRequest registerRequest) {
        JsonData jsonData = userService.registerUser(registerRequest);
        return jsonData;
    }

    /**
     * 用户登录接口
     *
     * @param loginRequest
     * @return
     */
    @ApiOperation("用户登录接口")
    @PostMapping("/login")
    public JsonData login(@ApiParam("登录对象")
                          @RequestBody UserLoginRequest loginRequest) {
        JsonData jsonData = userService.login(loginRequest);
        return jsonData;

    }

    @ApiOperation("获取用户详情")
    @GetMapping("/detail")
    public JsonData getUserDetail() {
        UserVO userVO = userService.getUserDetail();
        return JsonData.buildSuccess(userVO);
    }


}

