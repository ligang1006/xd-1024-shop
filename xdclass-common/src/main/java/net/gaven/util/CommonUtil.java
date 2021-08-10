package net.gaven.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * @author: lee
 * @create: 2021/8/7 10:22 上午
 **/
@Slf4j
public class CommonUtil {
    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "").substring(0, 32);
    }

    /**
     * 返回json给前端
     *
     * @param response
     * @param object
     */
    public static void sendJsonMessage(HttpServletResponse response, Object object) {
/**
 * jdk1.7之后，只要实现Closeable接口就能自动关闭（try里面）
 */
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");

        try (PrintWriter writer = response.getWriter();
        ) {
            writer.println(objectMapper.writeValueAsString(object));
            response.flushBuffer();
        } catch (Exception e) {
            log.error("响应json数据异常{}", e);
        }

    }
}
