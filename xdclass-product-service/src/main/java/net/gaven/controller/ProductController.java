package net.gaven.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.gaven.enums.BizCodeEnum;
import net.gaven.request.LockProductRequest;
import net.gaven.service.IProductService;
import net.gaven.util.JsonData;
import net.gaven.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lee
 * @since 2021-08-16
 */
@Api("商品模块")
@RestController
@RequestMapping("/api/product/v1")
public class ProductController {
    @Autowired
    private IProductService productService;

    @ApiOperation("商品列表接口")
    @GetMapping("/list")
    public JsonData getList(@ApiParam("当前页数") @RequestParam(value = "page", defaultValue = "1") Integer page,
                            @ApiParam("每页展示数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> list = productService.getList(page, size);
        return JsonData.buildSuccess(list);
    }

    @ApiOperation("商品详情")
    @GetMapping("/detail/{product_id}")
    public JsonData detail(@ApiParam("商品ID")
                           @PathVariable("product_id") Long productId) {
        ProductVO productVO = productService.productDetail(productId);
        return productVO == null ? JsonData.buildResult(BizCodeEnum.PRODUCT_NOT_EXITS) : JsonData.buildSuccess(productVO);
    }

    @ApiOperation("锁定商品")
    @GetMapping("/lock_product_stack")
    public JsonData lockProductStack(@ApiParam("锁定商品信息")
                                     @RequestParam("product_id") LockProductRequest lockProductRequest) {

        JsonData jsonData = productService.lockProductStack(lockProductRequest);
        return jsonData;
    }

}

