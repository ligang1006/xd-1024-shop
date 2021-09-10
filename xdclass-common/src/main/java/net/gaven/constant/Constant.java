package net.gaven.constant;

/**
 * @author: lee
 * @create: 2021/8/5 11:03 上午
 **/
public class Constant {

    public static final String REDIS_USER_SERVICE_PREFIX = "user-service:captcha";

    /**
     * 邮箱的主题
     */
    public static final String E_MAIL_SUBJECT = "邮箱验证码";
    /**
     * 邮箱的内容
     */
    public static final String E_MAIL_CONTENT = "欢迎来到召唤师峡谷";
    /**
     * token
     */
    public static final String REQUEST_TOKEN = "token";
    /**
     * 阿里支付回调成功返回
     */
    public static final String ALIPAY_SUCCESS = "success";
    /**
     * 阿里支付失败回调返回
     */
    public static final String ALIPAY_FAIL = "failure";

}
