package net.gaven.constant;

/**
 * @author: lee
 * @create: 2021/8/5 8:10 下午
 **/
public class CacheKey {
    /**
     * 注册验证码，第一个是类型，第二个是接收号码
     */
    public static final String CHECK_CODE_KEY = "code:%s:%s";
    /**
     * 购物车
     */
    public static final String CART_KEY = "cart:%s";
}
