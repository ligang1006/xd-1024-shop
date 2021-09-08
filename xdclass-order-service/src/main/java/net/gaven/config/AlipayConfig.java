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
    public static final String APP_PRI_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDh2PCkJ956QB49e3YK/RBAbAcTolDwKNa9qvmKq1LwZ74It+UDkf5YDkcQXAjsLri5A8B1j910ERIyv0UjIZUUu1gNry57vqA8gLHtvgthyRNh3897kqEXL8AxrEW83R/XjZVcVcqd6f2yOBW6N4lPj60qM90WTo/n2+I1wlXY5moRguB8NFoLC8Q1kAzQvjqILUIOjbqodoxcKRVS9RuAduNfE5XzmYFCDnB9RdtWXs10jfLG1pF37FY9RKOvMRNzlo5sH23HmDizHz29dUk9RRHge21hYgECP0hs8FHJBzPt6tIvbDE2Wu1uwQ3Bhrl4pURZ7d0CWr/cPRGaciEBAgMBAAECggEAL1QtKdmJEAl7zNqgpDDgRP/eg8jSOWH1jo3T+bFpkiPLeTxAa/0eVgm37r+6xvQLlgopPPnHKNmi/KuEq5YQDeYsz8FUdm4+Wi+GGhJnhDiFLU+fxX+27or9NeuqOagFUkDDejQoX+t3VO/X7cxRpDCx01RHErOoCKjVwPpWzzslWmVNdIwZFowFJosiHIj1u4Jt72dJ0AXeYoXUvaoKGEc0pb0TUlLXiosksWjYa/gZI7m4hxuzbUT8k0V/+zMxQ3fIJ3BNUPg0eH3HZRI/rZZnOinwxmX5BMRZDzuvm3TNxRO0Tz0EcQE+b3Fzcgnod9L96gAuJSYRjOJmMN2JIQKBgQDyvy/XxytWzwcHxGDq53VLOWzZr81dVz53AMx5y1qhcTXBC4v8s8Bp8yq3xw9v7I3EHVWuKRwRJn8JuTZKJCQONM6p09kHq8hZX3neDJtJg/+CB6hWKtwWGxJpV5GSzmmEE02HC29UZlz0WVOluVU3ETlR78chsKKE5EZkG0HlBQKBgQDuLY3myRAQt7q0RHFw4D5EjsIFZUCooptQgzWzgo5diN+9/ShdzKaKz/ByujXJ+hRX0V6WPknB4AOmp0i03ZD7bajFexOSj0a7CmL7DpRYrR30pA1N29hHh4b609GDwefjVuo2i2oPlkn1pCLnpmuxkV8XHeAL7jdN0Hw7IuOMzQKBgH7jAw4mlPfmdcVQmFyRqlUs6kILzCxbW1J3P2r2qiQzeiazc3QfPZfkPNMdoWse2qfFsbC82mf4mHUrtD4jEBnA7roE+7Av0iUtbBVuv4k5D0kX2Z1Y//wqIICh/n0fmjYopODPUF1suHAddUTuUKXdQfobfRqWKw2OCWFwggxFAoGBAN7Ig4PMrBdHE/+MSaPwTK4p7jfHxtw2BRshL/jx9KJu5gG7K6fZRipvaBSYMrnzDlY6Q8Q7DRiopiGbNbnfyb40i3n0rpEXLWzEwWLd90qe23c6gCtEqR/3F/3EZaRdmr6eTMOqUhG0XQfEeNW/Z9qXBKAF1My0DMzfZ2SVNM09AoGBAIvnGe74eaN+uXFUfDU8NC6lnZJDARU11SzHc3yuOVyl4Sr/dGEytWo4Y5F6XYEsWcO6b+VDMI5qMzXpVIAO51esiapR6R7grVadiExRP1xcx+0wqM3zjqQxH8NrUuxgTFXROzQaOSYOmcZy6BNEqLpyRO4GlXK4TIbvaw47hF8I";

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
