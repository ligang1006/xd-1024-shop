package net.gaven.service;

import net.gaven.vo.CartItemRequest;

/**
 * @author: lee
 * @create: 2021/8/17 9:17 上午
 **/
public interface ICartService {
    /**
     * 添加购物车
     *
     * @param cartItemRequest
     */
    void addCartItems(CartItemRequest cartItemRequest);

    /**
     * 清空购物车
     */
    void clear();
}
