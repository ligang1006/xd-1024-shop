package net.gaven.service;

import net.gaven.util.JsonData;
import net.gaven.vo.CartItemRequest;
import net.gaven.vo.CartItemVO;
import net.gaven.vo.CartVO;

import java.util.List;

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

    /**
     * 获取购物车信息
     *
     * @return
     */
    CartVO getMyCart();

    /**
     * 清空某个商品
     *
     * @param productId
     */
    void deleteItem(Long productId);

    /**
     * 修改购物车
     *
     * @param cartItemRequest
     */
    void changeMyCart(CartItemRequest cartItemRequest);

    /**
     * 确认商品信息
     *
     * @param productIdsList
     * @return
     */
    List<CartItemVO> confirmProductItems(List<Long> productIdsList);

}
