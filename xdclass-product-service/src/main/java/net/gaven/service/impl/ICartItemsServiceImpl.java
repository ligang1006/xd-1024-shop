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
import net.gaven.vo.CartVO;
import net.gaven.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        BoundHashOperations<String, Object, Object> myCart = getMyCartOps();

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

    private BoundHashOperations<String, Object, Object> getMyCartOps() {
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

    @Override
    public void clear() {
        String cartKey = getCartKey();
        redisTemplate.delete(cartKey);
    }

    @Override
    public void deleteItem(Long productId) {
        BoundHashOperations<String, Object, Object> myCartOps = getMyCartOps();
        String strProductId = String.valueOf(productId);
        myCartOps.delete(strProductId);
    }

    /**
     * 获取购物车最新的价钱
     *
     * @return
     */
    @Override
    public CartVO getMyCart() {
        //获取购物车
        log.info("获取购物车开始...");
        //获取全部购物项
        List<CartItemVO> cartItemVOList = buildCartItem(false);

        //封装成cartvo
        CartVO cartVO = new CartVO();
        cartVO.setCartItems(cartItemVOList);
        //更新商品价钱
        return cartVO;
    }

    /**
     * 获取最新的购物项，
     *
     * @param findLasted 是否获取最新价格
     * @return
     */
    private List<CartItemVO> buildCartItem(boolean findLasted) {
        //购物车商品列表
        List<CartItemVO> cartItemVOList = new ArrayList<>();
        //需要查询的商品列表
        List<Long> needQueryProducts = new ArrayList<>();
        BoundHashOperations<String, Object, Object> myCartOps = getMyCartOps();
        List<Object> values = myCartOps.values();
        for (Object value : values) {
            CartItemVO cartItemVO = JSON.parseObject((String) value, CartItemVO.class);
            log.info("获取的购物车信息为：{}", cartItemVO.toString());
            //这里获取的是为更新商品的数据
            cartItemVOList.add(cartItemVO);
            //需要更新的商品ID
            needQueryProducts.add(cartItemVO.getProductId());
        }
        //需要更新最新的商品信息
        if (findLasted) {
            refreshCartProduct(cartItemVOList, needQueryProducts);
        }

        return cartItemVOList;
    }

    private void refreshCartProduct(List<CartItemVO> cartItemVOList, List<Long> ids) {
        log.info("refresh lasted product info cartItemVOList {},ids{}", JSON.toJSONString(cartItemVOList), ids);
        if (CollectionUtils.isEmpty(cartItemVOList)) {
            return;
        }
        //新的商品信息
        List<ProductVO> productVOS = productService.getAllProductById(ids);
        //通过商品进行分类 K:商品Id V:商品信息VO,更新商品直接根据Id查询
        Map<Long, ProductVO> collects = productVOS.stream().collect(Collectors.toMap(ProductVO::getId, Function.identity()));

        productVOS.forEach(productVO -> {
            ProductVO lasted = collects.get(productVO.getId());
            productVO.setCoverImg(lasted.getCoverImg());
            productVO.setTitle(lasted.getTitle());

            productVO.setOldAmount(productVO.getAmount());
            productVO.setAmount(lasted.getAmount());
        });
    }

    /**
     * 修改购物车
     * 1、首先获取购物车
     * 2、设置商品数量
     *
     * @param cartItemRequest
     */
    @Override
    public void changeMyCart(CartItemRequest cartItemRequest) {
        long productId = cartItemRequest.getProductId();
        int buyNum = cartItemRequest.getBuyNum();
        if (buyNum < 0) {
            throw new BizException(BizCodeEnum.PRODUCT_NUM_NEGATIVE);
        }
        String strProductId = String.valueOf(productId);
        BoundHashOperations<String, Object, Object> myCartOps = getMyCartOps();
        Object o = myCartOps.get(strProductId);

        String jsonItem = "";
        jsonItem = (String) o;
        if (StringUtils.isEmpty(jsonItem)) {
            throw new BizException(BizCodeEnum.PRODUCT_NUM_NEGATIVE);
        }
        CartItemVO cartItemVO = JSON.parseObject(jsonItem, CartItemVO.class);
        cartItemVO.setBuyNum(buyNum);
        myCartOps.put(strProductId, JSON.toJSONString(cartItemVO));
    }
}
