package net.gaven.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author: lee
 * @create: 2021/8/9 12:28 下午
 **/
@Data
public class LoginUser {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 头像
     */
    @JsonProperty("head_img")
    private String headImg;

    /**
     * 邮箱
     */
    private String mail;
}
