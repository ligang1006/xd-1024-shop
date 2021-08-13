package net.gaven.handler;

import lombok.extern.slf4j.Slf4j;
import net.gaven.exception.BizException;
import net.gaven.util.JsonData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: lee
 * @create: 2021/8/4 8:36 上午
 **/
@ControllerAdvice
@Slf4j
public class CustomExceptionHandle {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public JsonData handlerException(Exception e) {
        //自定义异常
        if (e instanceof BizException) {
            BizException bizException = (BizException) e;
            log.info("[业务异常]{}", e);
            return JsonData.buildError(bizException.getMessage());
        } else {
            log.info("[非业务异常]{}", e);
            return JsonData.buildError("全局异常，未知错误" + e.getMessage());
        }
    }
}
