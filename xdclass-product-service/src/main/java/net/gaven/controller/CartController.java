package net.gaven.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.gaven.service.ICartService;
import net.gaven.util.JsonData;
import net.gaven.vo.CartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
