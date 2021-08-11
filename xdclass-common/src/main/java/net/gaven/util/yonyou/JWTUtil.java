package net.gaven.util.yonyou;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.gaven.model.LoginUser;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 生成jet和解码jwt的工具类
 *
 * @author: lee
 * @create: 2021/8/9 12:24 下午
 **/
public class JWTUtil {
    /**
     * token 过期时间，正常是7天，方便测试我们改为70
     */
    private static final long EXPIRE = 1000 * 60 * 60 * 24 * 7 * 10;

    /**
     * 加密的秘钥
     */
    private static final String SECRET = "xdclass.net666";

    /**
     * 令牌前缀
     */
    private static final String TOKEN_PREFIX = "xdclass1024shop";

    /**
     * subject
     */
    private static final String SUBJECT = "xdclass";

    /**
     * 创建token
     *
     * @param loginUser
     * @return
     */
    public static String generateJsonWebToken(LoginUser loginUser) {
        //Claims claims = Jwts.claims().setSubject("Me");
        //       String jwt = Jwts.builder().setClaims(claims).compact();
        if (loginUser == null) {
            throw new NullPointerException("loginUser对象为空");
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("head_img", loginUser.getHeadImg())
                .claim("id", loginUser.getId())
                .claim("name", loginUser.getName())
                .claim("mail", loginUser.getMail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
        token = TOKEN_PREFIX + token;
        return token;
    }

    /**
     * 校验token
     *
     * @param token
     * @return
     */
    public static Claims checkToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Claims body = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replaceAll(TOKEN_PREFIX, "")).getBody();
        return body;
    }

    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZGNsYXNzIiwiaWF0IjoxNjI4NDg0NjI0LCJleHAiOjE2MzAyMzc2NTd9.7Nw6WQSV8G3MU-RQKawmQfXlzVHOErgW3yQ-pyESL5I";
        Claims claims = checkToken(token);
        System.out.println(claims);
    }
}
