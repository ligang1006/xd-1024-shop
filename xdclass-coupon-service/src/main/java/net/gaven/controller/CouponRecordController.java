package net.gaven.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.gaven.service.ICouponRecordService;
import net.gaven.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lee
 * @since 2021-08-12
 */
@Api("优惠卷记录接口")
@RestController
@RequestMapping("/api/coupon_record/v1")
public class CouponRecordController {
    @Autowired
    private ICouponRecordService recordService;

    @ApiOperation("获取优惠卷记录列表")
    @GetMapping("/page")
    public JsonData getPage(@ApiParam("页数") @RequestParam(value = "page", defaultValue = "1") Integer page,
                            @ApiParam("每页数量") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> res = recordService.page(page, size);
        return JsonData.buildSuccess(res);
    }
}

