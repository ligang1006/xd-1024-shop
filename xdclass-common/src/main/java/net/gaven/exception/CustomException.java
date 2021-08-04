package net.gaven.exception;

import io.swagger.models.auth.In;
import net.gaven.enums.BizCodeEnum;
import org.springframework.stereotype.Component;

/**
 * @author: lee
 * @create: 2021/8/4 8:32 上午
 **/

public class CustomException extends RuntimeException {

    private Integer code;
    private String msg;

    public CustomException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public CustomException(BizCodeEnum bizCodeEnum) {
        super(bizCodeEnum.getMessage());
        this.code = bizCodeEnum.getCode();
        this.msg = bizCodeEnum.getMessage();
    }

}
