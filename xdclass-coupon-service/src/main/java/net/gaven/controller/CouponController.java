package net.gaven.controller;

import lombok.extern.slf4j.Slf4j;
import net.gaven.util.MyRedissionClient;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.gaven.enums.CouponCategoryEnum;
import net.gaven.service.ICouponService;
import net.gaven.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lee
 * @since 2021-08-12
 */
@Slf4j
@Api("优惠券接口")
@RestController
@RequestMapping("/api/coupon/v1")
public class CouponController {
    @Autowired
    private ICouponService couponService;
    //    @Autowired
//    private MyRedissionClient redissionClient;
    @Autowired
    private RedissonClient redissonClient;

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

    @ApiOperation("获取优惠卷")
    @GetMapping("/add/promotion/{coupon_id}")
    public JsonData addCoupon(@ApiParam("优惠卷Id")
                              @PathVariable("coupon_id") Long couponId) {
        return couponService.addCoupon(couponId, CouponCategoryEnum.PROMOTION);
    }

    @GetMapping("/test_redission/{coupon_id}")
    public JsonData testRedission(@PathVariable("coupon_id") Long couponId) {
        RLock lock = redissonClient.getLock("lock:coupon:" + couponId);
        try {
            lock.lock(60, TimeUnit.SECONDS);
            log.info("加锁成功" + couponId + ":" + Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(6);
        } catch (Exception e) {
            log.error("get error", e);
        } finally {

            lock.unlock();
            log.info("un lock success" + "couponId:" + couponId + "thread" + Thread.currentThread().getName());
        }

        return JsonData.buildSuccess("get lock success");
    }


}

