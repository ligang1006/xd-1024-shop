package net.gaven.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lee
 * @create: 2021/8/7 2:54 下午
 **/
@Data
public class UserRegisterRequest {

    private Long id;
    @ApiModelProperty(value = "昵称", example = "Anna")
    private String name;

    @ApiModelProperty(value = "密码", example = "12345")
    private String pwd;


    @ApiModelProperty(value = "头像", example = "https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg")
    @JsonProperty("head_img")
    private String headImg;

    @ApiModelProperty(value = "用户个人性签名", example = "人生需要动态规划，学习需要贪心算法")
    private String slogan;

    @ApiModelProperty(value = "0表示女，1表示男", example = "1")
    private Integer sex;

    @ApiModelProperty(value = "邮箱", example = "794666918@qq.com")
    private String mail;

    @ApiModelProperty(value = "验证码", example = "232343")
    private String code;
}
