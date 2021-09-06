package net.gaven.feign;

import net.gaven.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: lee
 * @create: 2021/9/4 4:50 下午
 **/
@FeignClient("xdclass-coupon-service")
public interface CouponFeignService {
    /**
     * 获取优惠卷详情
     *
     * @param recordId
     * @return
     */
    @GetMapping("/api/coupon_record/v1/detail/{record_id}")
    JsonData detail(@PathVariable(value = "record_id") Long recordId);
}