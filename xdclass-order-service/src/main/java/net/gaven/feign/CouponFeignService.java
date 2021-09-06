package net.gaven.feign;

import net.gaven.request.LockCouponRecordRequest;
import net.gaven.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    /**
     * 锁定优惠卷接口
     *
     * @param lockCouponRecordRequest
     * @return
     */
    @PostMapping("/api/coupon_record/v1/lock_coupon")
    JsonData lockCouponRecords(@RequestBody LockCouponRecordRequest lockCouponRecordRequest);
}