package net.gaven.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.gaven.enums.BizCodeEnum;
import net.gaven.request.LockCouponRecordRequest;
import net.gaven.service.ICouponRecordService;
import net.gaven.util.JsonData;
import net.gaven.vo.CouponRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("获取优惠卷记录详情")
    @GetMapping("/detail/{record_id}")
    public JsonData detail(@ApiParam("优惠卷Id")
                           @PathVariable(value = "record_id") Long recordId) {
        CouponRecordVO recordVO = recordService.detail(recordId);
        return recordVO == null
                ? JsonData.buildResult(BizCodeEnum.COUPON_NO_EXITS)
                : JsonData.buildSuccess(recordVO);
    }

    @ApiOperation("锁定优惠卷")
    @GetMapping("/lock_coupon")
    public JsonData lockCoupon(@ApiParam("锁定优惠卷")
                               @RequestBody LockCouponRecordRequest lockCouponRecordRequest) {
        JsonData jsonData = recordService.lockCouponRecords(lockCouponRecordRequest);
        return jsonData;
    }
}

