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
     */
    public static final  String PAY_GATEWAY="https://openapi.alipaydev.com/gateway.do";


    /**
     * 支付宝 APPID TODO
     */
    public static final  String APPID="2016092000555936";

    /**
     * 应用私钥 TODO
     */
    public static final String APP_PRI_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDCzvo38GtB4CxDAcIU6my5GzrISZJPYpK/4ij0GdY6Ej2RaeFF4o+Kc1KW5fgnCuDO13M+4+AO+r5ItbFyFe/rK5d6LVBsyjEisVhGivCBZC6fmzCEsQpqVe7kON0iznRT83YSyHq6Oax2q0DRB5d8AQf26KC2WRGVfNlLE75k2mf6pu8ZThrTtVMQ4xBcmdCNDfrT+77MxvfD+1GbSopGuBObykM6HRoGHiANW5knmdOAwf+3P+ZPTbQVrYMaHTXxp/9+66F43hxyMiboAk+9+V6XkE79Wty4wY7R4DYn8BOCZiq6VNqwDoLJfVhz115cFgMT7stlgEVm3URGeJtbAgMBAAECggEAb7EJ516xsEBeQEDWzPUnRu4kwsbj2x4SM2/wuRvDJuJkaGYDYfUnBWNfW+MYZxImmJEQo2M1iIEc7kU2KsoHF5eTrtoiPW2jxFdX3II2qIkO7jWlGL8E0LjaUgGEUDt8mNGCxfwp2XHNvvArJm2q3c8diOQRmNryaPn+/7ufMuTBp9fq20szEXA1K0NcqeoU/PjN+TN/NSzAVYDakGiOgG10z/Z/K04maCDzNGm8c/IKGe7L0rWDhx1Z+ssf7Yxan43PwWygA/IeWfoBICYKfN9V7MLt66rKC5yQ0TxxLaof0vijWGVQAh+1gGVquaGT++d+B/OebAw5wehukwqwiQKBgQDs8wpk1rFDePoW/wurbvr6LiN43KMqpSRihmcZYnjtsSSuqtcn0K3R+zw6zfzrI7VjtMzmihlfLWlK0dg3PMWC8IiVGvam4M3/SpTelqDnNqngyYs+QLEVc02OIAIdvPFw30VzOgtpur5FEsdF7UDmRRbf303b1iHMWw/04U6o5QKBgQDSeJTX6DCJ5kD2s/J/PQA8vj7afquUR5om2prLMqizMKeLryIw7AZejsYMX1LKuj8vo3Rf2fYCtbjvwN9JQehvLmK2cxtHX4HJldjSKkQE8yKIltJPdeepyB+C4vQo9ZOgWO4+XzEDhHUU7F8Mbz6FrbYi4CK9+hyA5NEFG/8vPwKBgAmT1VK4HtgkVq+BRZ57UOe137ct6+Z4YF0fI6GyCkNJSrtIqoOweqYnKosR8mwquFU8ZJ/Y6yeetUfR7ZRl/3HIeSzVtyyNn6X3BMdufTh7TCiK3Pb6NG4yh+r7vjonffba/3ql2PpAExrFpIpeCaaFGmSYKRR+tkUFVULHrJ9FAoGAP9WspzNYDWsVhv2a7fZtbP7yh0hl98ojuecRjdGmkvqNxnLuttHPOLyq2rPKo4wLgAJeWHf+AmJlXeosjsZ7ncT+0YFl46bW8XIOWefRy4KGldDQpqMHCVqWHBeDoCyjgr2WPLqYXg29iQQL6/SNjPPbywYZ427JWFqN/rO4GPcCgYEAswOhKXEcVGVsVFe6Yyxdfbq0yJuEIu52SHmiVWyPpOYM0lZ+EmPBrTLpnmQ/68kljOIHF197jXi1QURQ1rQNIxtXyZJZ57qaf7CrpI476rJF+JgEkXu7NF6+FrP9irZGgLEJUY9bB2vJKYDMWDTWqwnHPh/uhIiHlFVUGVAa28w=";
    /**
     * 支付宝公钥 TODO
     */
    public static final String ALIPAY_PUB_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk8OPeu/Bhbxr8chgRXnDSm0FDMZoK3r/qLLzqbv+L2/mYTLxHKw5U3c5XPHXar5SVWRp+IgLsZdTfYeZ+jYFmY+dTYk8mG8GXc1uYwHFADcWQAcR4KA3cbxiaubg7g//ECEhwA5CmaosjJ2p8UAz4j7cDB37TDON7oGMFbkXnSBlGhj5oy6rxdUXS+KeG9YmRPkQU3x3ljK37RRLFhwPPRR218IGDCmCtnI5ddKEq1hMAPnYmKNezerxSGGxxIaggt+sDwN1S3GNNSU7AelLPuvNEza49KaQijsBJpaLKUvqt5KO4IVfvPCmRsIFe7KzgdEmjygIY81qXacPzopJIQIDAQAB";

    public static final String APP_PUB_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAws76N/BrQeAsQwHCFOpsuRs6yEmST2KSv+Io9BnWOhI9kWnhReKPinNSluX4JwrgztdzPuPgDvq+SLWxchXv6yuXei1QbMoxIrFYRorwgWQun5swhLEKalXu5DjdIs50U/N2Esh6ujmsdqtA0QeXfAEH9uigtlkRlXzZSxO+ZNpn+qbvGU4a07VTEOMQXJnQjQ360/u+zMb3w/tRm0qKRrgTm8pDOh0aBh4gDVuZJ5nTgMH/tz/mT020Fa2DGh018af/fuuheN4ccjIm6AJPvflel5BO/VrcuMGO0eA2J/ATgmYqulTasA6CyX1Yc9deXBYDE+7LZYBFZt1ERnibWwIDAQAB";
    /**
     * 签名类型
     */
    public static final  String SIGN_TYPE="RSA2";


    /**
     * 字符编码
     */
    public static final  String CHARSET="UTF-8";


    /**
     * 返回参数格式
     */
    public static final  String FORMAT="json";


    /**
     * 构造函数私有化
     */
    private AlipayConfig(){

    }


    private volatile static AlipayClient instance = null;


    /**
     * 单例模式获取, 双重锁校验
     * @return
     */
    public static AlipayClient getInstance(){

        if(instance==null){
            synchronized (AlipayConfig.class){
                if(instance == null){
                    instance = new DefaultAlipayClient(PAY_GATEWAY,APPID,APP_PRI_KEY,FORMAT,CHARSET,ALIPAY_PUB_KEY,SIGN_TYPE);
                }
            }
        }
        return instance;
    }

}
