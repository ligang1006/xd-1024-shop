package net.gaven.feign;

import io.swagger.annotations.ApiParam;
import net.gaven.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: lee
 * @create: 2021/9/3 4:23 下午
 **/
@FeignClient("xdclass-user-service")
public interface UserFeignService {

    /**
     * 获取地址详情，本身有防止水平越权查询
     *
     * @param addressId
     * @return
     */
    @GetMapping("/api/v1/address/detail/{address_id}")
    JsonData addressDetail(@PathVariable("address_id") Long addressId);
}
