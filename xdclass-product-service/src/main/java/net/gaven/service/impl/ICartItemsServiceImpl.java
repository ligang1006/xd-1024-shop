package net.gaven.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.gaven.constant.CacheKey;
import net.gaven.enums.BizCodeEnum;
import net.gaven.exception.BizException;
import net.gaven.interceptor.LoginInterceptor;
import net.gaven.model.LoginUser;
import net.gaven.service.ICartService;
import net.gaven.service.IProductService;
import net.gaven.vo.CartItemRequest;
import net.gaven.vo.CartItemVO;
import net.gaven.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import springfox.documentation.spring.web.json.Json;

/**
 * @author: lee
 * @create: 2021/8/17 9:19 上午
 **/
@Slf4j
@Service
public class ICartItemsServiceImpl implements ICartService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IProductService productService;

    /**
     * 添加购物车
     * 购物车的数据结构Map<String,Map<String,Object>>
     * k:userId购物车的唯一Id HK 商品Id HV:商品信息Json
     * redis对应的数据结构 BoundHashOperations<String, Object, Object>
     * <p>
     * 1、先获取用户信息
     * 2、添加拦截器
     * 3、 获取商品信息
     * <p>
     * 判断购物车之前是否有商品
     * 有：更改数量
     * 无：new
     *
     * @param cartItemRequest
     */
    @Override
    public void addCartItems(CartItemRequest cartItemRequest) {
        Long productIdL = cartItemRequest.getProductId();
        Integer buyNum = cartItemRequest.getBuyNum();
        String productId = String.valueOf(productIdL);
        //获取购物车
        BoundHashOperations<String, Object, Object> myCart = getMyCart();

        //获取商品
        Object o = myCart.get(productId);
        String product = "";
        if (o != null) {
            product = (String) o;
        }
        ProductVO productVO = productService.productDetail(productIdL);
        if (productVO == null) {
            throw new BizException(BizCodeEnum.PRODUCT_NOT_EXITS);
        }
        //新添加的商品
        if (StringUtils.isEmpty(product)) {
            CartItemVO cartItemVO = new CartItemVO();
            cartItemVO.setBuyNum(buyNum);
            cartItemVO.setProductId(productIdL);

            cartItemVO.setAmount(productVO.getAmount());
            cartItemVO.setProductImg(productVO.getCoverImg());
            cartItemVO.setProductTitle(productVO.getTitle());
            myCart.put(productId, JSON.toJSONString(cartItemVO));
        } else {
            //之前购物车中存在商品
            CartItemVO cartItemVO = JSON.parseObject(product, CartItemVO.class);
            cartItemVO.setBuyNum(cartItemVO.getBuyNum() + buyNum);
            myCart.put(productId, JSON.toJSONString(cartItemVO));
        }

    }

    private BoundHashOperations<String, Object, Object> getMyCart() {
        String cartKey = getCartKey();
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(cartKey);
        return boundHashOperations;
    }

    /**
     * 获取购物车的key
     *
     * @return
     */
    private String getCartKey() {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        String cartKey = String.format(CacheKey.CART_KEY, loginUser.getId());
        return cartKey;
    }
}
