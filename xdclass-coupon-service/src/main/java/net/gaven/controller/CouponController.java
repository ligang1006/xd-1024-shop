package net.gaven.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.gaven.service.ICouponService;
import net.gaven.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lee
 * @since 2021-08-12
 */
@Api("优惠券接口")
@RestController
@RequestMapping("/api/coupon/v1")
public class CouponController {
    @Autowired
    private ICouponService couponService;

    /**
     * @return
     */
    @ApiOperation("获取优惠券列表")
    @GetMapping("/page_coupon")
    public JsonData pageCoupon(@ApiParam("当前页数") @RequestParam(value = "page", defaultValue = "1") Integer page,
                               @ApiParam("每页展示数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> pageMap = couponService.pageCouponActivity(page, size);
        return JsonData.buildSuccess(pageMap);
    }

}

