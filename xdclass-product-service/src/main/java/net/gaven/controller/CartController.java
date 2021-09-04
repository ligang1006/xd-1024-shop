package net.gaven.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.gaven.service.ICartService;
import net.gaven.util.JsonData;
import net.gaven.vo.CartItemRequest;
import net.gaven.vo.CartItemVO;
import net.gaven.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: lee
 * @create: 2021/8/17 9:25 上午
 **/
@Api("购物车")
@RestController
@RequestMapping(("/api/cart/v1"))
public class CartController {
    @Autowired
    private ICartService cartService;

    @ApiOperation("添加购物车")
    @PostMapping("/add")
    public JsonData addCart(@ApiParam("购物车商品")
                            @RequestBody CartItemRequest cartItemRequest) {
        cartService.addCartItems(cartItemRequest);
        return JsonData.buildSuccess();
    }

    @ApiOperation("清空购物车,没有也不报错")
    @DeleteMapping("/clear")
    public JsonData clearCart() {
        cartService.clear();
        return JsonData.buildSuccess();
    }

    @ApiOperation("清空购物车的某个商品")
    @DeleteMapping("/delete/{product_id}")
    public JsonData deleteItem(@ApiParam("删除的商品ID")
                               @PathVariable("product_id") Long productId) {
        cartService.deleteItem(productId);
        return JsonData.buildSuccess();
    }

    @ApiOperation("查询购物车")
    @GetMapping("/my_cart")
    public JsonData myCart() {
        CartVO cartVO = cartService.getMyCart();
        return JsonData.buildSuccess(cartVO);
    }

    @ApiOperation("修改购物车")
    @PostMapping("/change")
    public JsonData changeCart(@ApiParam("修改购物车")
                               @RequestBody CartItemRequest cartItemRequest) {
        cartService.changeMyCart(cartItemRequest);
        return JsonData.buildSuccess();
    }

    @ApiOperation("确认商品信息")
    @PostMapping("/confirm_order_cart_items")
    JsonData confirmProductItems(@ApiParam("商品ID列表")
                                 @RequestBody List<Long> productIdsList) {
        List<CartItemVO> cartItemVOS = cartService.confirmProductItems(productIdsList);
        return JsonData.buildSuccess(cartItemVOS);
    }
}
