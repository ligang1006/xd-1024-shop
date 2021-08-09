package net.gaven.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lee
 * @create: 2021/8/9 8:44 上午
 **/
@Data
@ApiModel(value = "登录对象", description = "用户登录对象")
public class UserLoginRequest {
    @ApiModelProperty(value = "邮箱", example = "124@qq.com")
    private String mail;
    @ApiModelProperty(value = "密码", example = "123456")
    private String pwd;
}
