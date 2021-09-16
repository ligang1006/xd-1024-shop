package net.gaven.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;

/**
 * @author: lee
 * @create: 2021/9/8 9:14 上午
 **/

public class AlipayConfig {
    /**
     * 支付宝网关地址  TODO
     * https://openapi.alipaydev.com/gateway.do
     */
    public static final String PAY_GATEWAY = "https://openapi.alipaydev.com/gateway.do";


    /**
     * 支付宝 APPID TODO
     */
    public static final String APPID = "2021000118618112";

//    /**
//     * 应用私钥 TODO
//     */
//    public static final String APP_PRI_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDCzvo38GtB4CxDAcIU6my5GzrISZJPYpK/4ij0GdY6Ej2RaeFF4o+Kc1KW5fgnCuDO13M+4+AO+r5ItbFyFe/rK5d6LVBsyjEisVhGivCBZC6fmzCEsQpqVe7kON0iznRT83YSyHq6Oax2q0DRB5d8AQf26KC2WRGVfNlLE75k2mf6pu8ZThrTtVMQ4xBcmdCNDfrT+77MxvfD+1GbSopGuBObykM6HRoGHiANW5knmdOAwf+3P+ZPTbQVrYMaHTXxp/9+66F43hxyMiboAk+9+V6XkE79Wty4wY7R4DYn8BOCZiq6VNqwDoLJfVhz115cFgMT7stlgEVm3URGeJtbAgMBAAECggEAb7EJ516xsEBeQEDWzPUnRu4kwsbj2x4SM2/wuRvDJuJkaGYDYfUnBWNfW+MYZxImmJEQo2M1iIEc7kU2KsoHF5eTrtoiPW2jxFdX3II2qIkO7jWlGL8E0LjaUgGEUDt8mNGCxfwp2XHNvvArJm2q3c8diOQRmNryaPn+/7ufMuTBp9fq20szEXA1K0NcqeoU/PjN+TN/NSzAVYDakGiOgG10z/Z/K04maCDzNGm8c/IKGe7L0rWDhx1Z+ssf7Yxan43PwWygA/IeWfoBICYKfN9V7MLt66rKC5yQ0TxxLaof0vijWGVQAh+1gGVquaGT++d+B/OebAw5wehukwqwiQKBgQDs8wpk1rFDePoW/wurbvr6LiN43KMqpSRihmcZYnjtsSSuqtcn0K3R+zw6zfzrI7VjtMzmihlfLWlK0dg3PMWC8IiVGvam4M3/SpTelqDnNqngyYs+QLEVc02OIAIdvPFw30VzOgtpur5FEsdF7UDmRRbf303b1iHMWw/04U6o5QKBgQDSeJTX6DCJ5kD2s/J/PQA8vj7afquUR5om2prLMqizMKeLryIw7AZejsYMX1LKuj8vo3Rf2fYCtbjvwN9JQehvLmK2cxtHX4HJldjSKkQE8yKIltJPdeepyB+C4vQo9ZOgWO4+XzEDhHUU7F8Mbz6FrbYi4CK9+hyA5NEFG/8vPwKBgAmT1VK4HtgkVq+BRZ57UOe137ct6+Z4YF0fI6GyCkNJSrtIqoOweqYnKosR8mwquFU8ZJ/Y6yeetUfR7ZRl/3HIeSzVtyyNn6X3BMdufTh7TCiK3Pb6NG4yh+r7vjonffba/3ql2PpAExrFpIpeCaaFGmSYKRR+tkUFVULHrJ9FAoGAP9WspzNYDWsVhv2a7fZtbP7yh0hl98ojuecRjdGmkvqNxnLuttHPOLyq2rPKo4wLgAJeWHf+AmJlXeosjsZ7ncT+0YFl46bW8XIOWefRy4KGldDQpqMHCVqWHBeDoCyjgr2WPLqYXg29iQQL6/SNjPPbywYZ427JWFqN/rO4GPcCgYEAswOhKXEcVGVsVFe6Yyxdfbq0yJuEIu52SHmiVWyPpOYM0lZ+EmPBrTLpnmQ/68kljOIHF197jXi1QURQ1rQNIxtXyZJZ57qaf7CrpI476rJF+JgEkXu7NF6+FrP9irZGgLEJUY9bB2vJKYDMWDTWqwnHPh/uhIiHlFVUGVAa28w=";
//    /**
//     * 支付宝公钥 TODO
//     */
//    public static final String ALIPAY_PUB_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk8OPeu/Bhbxr8chgRXnDSm0FDMZoK3r/qLLzqbv+L2/mYTLxHKw5U3c5XPHXar5SVWRp+IgLsZdTfYeZ+jYFmY+dTYk8mG8GXc1uYwHFADcWQAcR4KA3cbxiaubg7g//ECEhwA5CmaosjJ2p8UAz4j7cDB37TDON7oGMFbkXnSBlGhj5oy6rxdUXS+KeG9YmRPkQU3x3ljK37RRLFhwPPRR218IGDCmCtnI5ddKEq1hMAPnYmKNezerxSGGxxIaggt+sDwN1S3GNNSU7AelLPuvNEza49KaQijsBJpaLKUvqt5KO4IVfvPCmRsIFe7KzgdEmjygIY81qXacPzopJIQIDAQAB";
    /**
     * 应用私钥 TODO
     */
    public static final String APP_PRI_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC28Lbg0s8/3Llc0c4Av9vMURxX7ra4HLZ3vqywodglktIfw5xl+Hy/NmTozFExkekzmF0fMYOS9u4VzD1xZu1nE5q2Ju7m+NjR+wVy4+6GyBF4jacj7uHcRBEbP2YBy0cbhWF39+o+CzndMo0xYAtgmgWSf1+q+4VR/6kIegaQAIf6gJOsnTB0H6nSLQx35QCdIrEz8YuoJ1h++21jjSuZHNP8he+6QRNNvLBr9/vHuoOtoE9kgxgOY7a6cJV+6wr+x+9PSbayREKvngzeQTJMvxc0wzRrWyKN9JXhb7oFlGzwq7eygGLLUQhAidIm0Wmx0Mh46FzYxwIp2nzO2ySHAgMBAAECggEBAKoYIIHjoI1pZfP2o0qxGOXdGLRtoPiIweOOpzRIFxfAbpzYHOf25c5H/gUyS+ACIck2RodFW2ffLjmYQBzbraqkYLoycvjQsZBzjHppkZNCHHRxiNFxQBG0PA+QoADVB9V/68NHAEYV3AReB0e8XHfCXaH7ynDJEgnO41SdWNK0CUw0KA9C1UOe+F2vw//m6qOEzf+7GU0NfsfKUr27ZkTn0t2F6HO8TgATQL0DMXDWukVqtnPvJIny8SXy2AyQKwugItw1xXJ1g0z96/QRMA/kchkDoaCr9yx3kn84+3WIl6c4nnlM9GmlgYTeT9mlNDHZAk8c7CVxRYCScFjKtCECgYEA2HYWxkucMD5gtaHuapV1Lh1nZcENQf+OQq1eoqcZnsg+qrmgpsjBUbwXqTwC3H8fjxxTgg6SDA5ZYUFFc7GgzzUPH0sGHWP2DWI24G0vZ/Xwqudfpqw7Jm5OQR087E3Rk3+u+cyPahAZOJCwYkXOWLCAV3k3mFmbriEBWrKVTVcCgYEA2Fsn1pCwHf2GRXddvllaIROKXp3aUF8YPUide5+GiQeCGe3/sIeTRIGNkK7a4BsZ9heNtg3dA3m9GC2kwbkW9rmwe9cKKmw1tTSpRoXTt5a73Ll0BzGL2Nk69hSmPcHk6oYyrqgW5rbMdzzN8ZhnBrUxHdo02RKbyUHN1zxYNFECgYBHEydRPxxu3tyEZ0JQrviGBtOfnq8Kv63niOOtBq8r6tZg2oi8muiXPOqMAA90dS1YoVVuO/iKit+gRn1Wirmhr+tiPBEq71qodFpOgKtzOFco1sYahSz65nc9fCs38CljhCZhEWD504VkhGXWLtOdXX8Wuc+eb7XljCLUtBTACwKBgEYoqS6HFNVk1RnJr3h+eR+ouoIcJiNR8wZMeeUQwgvFh4bpCrUwAa2BZWq5iPiLUUvgL3AN5tGttIgWpQq8/plbdcNy5DUFihUM0qev1gv+sUTOTrXWd0cGinwqDg6Qxa7Xfa/tih+ev4cbDJ1/kkk2kIrhq6mIw0PsNtfJFcFBAoGAH563ltS5XZuN+isycv8rhxAu/ImIh7QtgmR/hyImm8IABtPxpXtu68IJBCUTUZ52CGbeKfy7EQEalaVCw5XDCdF1U40kdgLpe6MkdCs8LoF1SCjAGxQaazp4Lh11Br5/E8Hzhvn7ylK71cp0KzQ5rQGeLsR2q+HBq2jrR3TO3jQ=";
    /**
     * 支付宝公钥 TODO
     */
    public static final String ALIPAY_PUB_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk8L2LaGIUsBy9Q+DpEb8KI8LxRiw3mppOYFPQ0nwU/IGcyjQPNPGWC1xBdg9q60pDzV89I9uZ4OtiW1LVUmDbT6ELc5V7GS8M6k2fgVDXgkg6RP8eHRB91vLtAklMB9wPTvGvw1uZhnuqKpI3uxUkjKPJ7Pa3XCY/IW6LkAwEhUzySI8+Oqu5PUYjw/gz/9j37n8glmLKYm8C5rDZkYgrhtgQ/nGmrlFhBQzuA4hcVvgL0jjBOh3UVXZ/hP8DhDJq5BTkrEuIDYDntewA0rAndQAOrEaLGVd1pAu4bSaDlkKlY6b+YvD8ZHVk0EmrrcGMjxK+Xa4zWsDZ+j9ITvSjwIDAQAB";
    /**
     * 应用公钥
     */
    public static final String APP_PUB_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtvC24NLPP9y5XNHOAL/bzFEcV+62uBy2d76ssKHYJZLSH8OcZfh8vzZk6MxRMZHpM5hdHzGDkvbuFcw9cWbtZxOatibu5vjY0fsFcuPuhsgReI2nI+7h3EQRGz9mActHG4Vhd/fqPgs53TKNMWALYJoFkn9fqvuFUf+pCHoGkACH+oCTrJ0wdB+p0i0Md+UAnSKxM/GLqCdYfvttY40rmRzT/IXvukETTbywa/f7x7qDraBPZIMYDmO2unCVfusK/sfvT0m2skRCr54M3kEyTL8XNMM0a1sijfSV4W+6BZRs8Ku3soBiy1EIQInSJtFpsdDIeOhc2McCKdp8ztskhwIDAQAB";
    /**
     * 签名类型
     */
    public static final String SIGN_TYPE = "RSA2";


    /**
     * 字符编码
     */
    public static final String CHARSET = "UTF-8";


    /**
     * 返回参数格式
     */
    public static final String FORMAT = "json";


    /**
     * 构造函数私有化
     */
    private AlipayConfig() {

    }


    private volatile static AlipayClient instance = null;


    /**
     * 单例模式获取, 双重锁校验
     *
     * @return
     */
    public static AlipayClient getInstance() {

        if (instance == null) {
            synchronized (AlipayConfig.class) {
                if (instance == null) {
                    instance = new DefaultAlipayClient(PAY_GATEWAY, APPID, APP_PRI_KEY, FORMAT, CHARSET, ALIPAY_PUB_KEY, SIGN_TYPE);
                }
            }
        }
        return instance;
    }

}
