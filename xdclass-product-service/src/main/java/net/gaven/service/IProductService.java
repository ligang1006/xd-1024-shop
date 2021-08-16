package net.gaven.service;

import net.gaven.util.JsonData;

import java.util.Map;

/**
 * @author: lee
 * @create: 2021/8/16 9:37 上午
 **/
public interface IProductService {
    /**
     * 获取商品列表
     *
     * @param page
     * @param size
     * @return
     */
    Map<String, Object> getList(Integer page, Integer size);
}
