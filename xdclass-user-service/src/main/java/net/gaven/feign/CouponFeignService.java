package net.gaven.feign;

import net.gaven.request.NewUserCouponRequest;
import net.gaven.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author: lee
 * @create: 2021/8/19 9:28 上午
 **/
@FeignClient(name = "xdclass-coupon-service")
public interface CouponFeignService {

    /**
     * 新用户注册发放优惠券
     *
     * @param request
     * @return
     */
    @PostMapping("/api/coupon/v1/new_user_coupon")
    JsonData getNewCoupon(NewUserCouponRequest request);
}
