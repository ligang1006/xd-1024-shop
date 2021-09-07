package net.gaven.feign;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.gaven.request.LockProductRequest;
import net.gaven.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author: lee
 * @create: 2021/9/4 3:01 下午
 **/
@FeignClient("xdclass-product-service")
public interface ProductFeignService {
    /**
     * 确认商品信息
     *
     * @param productIdsList
     * @return
     */
    @PostMapping("/api/cart/v1/confirm_order_cart_items")
    JsonData confirmProductItems(@RequestBody List<Long> productIdsList);

    /**
     * 锁定商品库存
     *
     * @param lockProductRequest
     * @return
     */
    @PostMapping("/api/product/v1/lock_records")
    JsonData lockProductStack(@RequestBody LockProductRequest lockProductRequest);

}
