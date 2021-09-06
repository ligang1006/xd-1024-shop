package net.gaven.feign;

import net.gaven.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}
