package net.gaven.vo;

import lombok.Data;

/**
 * @author: lee
 * @create: 2021/8/16 12:42 下午
 **/
@Data
public class BannerVO {
    private Integer id;

    /**
     * 图片
     */
    private String img;

    /**
     * 跳转地址
     */
    private String url;

    /**
     * 权重
     */
    private Integer weight;
}
