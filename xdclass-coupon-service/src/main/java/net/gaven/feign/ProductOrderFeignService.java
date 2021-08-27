package net.gaven.feign;

import net.gaven.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: lee
 * @create: 2021/8/26 9:44 上午
 **/
@FeignClient(name = "xdclass-order-service")
public interface ProductOrderFeignService {
    @GetMapping("/api/order/v1/query_state")
    JsonData getOrderProductStatus(@RequestParam("out_trade_no")String outTradeNo);
}
