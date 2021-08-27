package net.gaven.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.gaven.enums.BizCodeEnum;
import net.gaven.service.IOrderService;
import net.gaven.util.JsonData;
import net.gaven.vo.ConfirmOrderRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lee
 * @since 2021-08-17
 */
@RestController
@RequestMapping("/api/order/v1")
public class ProductOrderController {

    @Autowired
    IOrderService orderService;

    @ApiOperation("提交订单")
    @PostMapping("/confirm")
    public void confirmOrder(@ApiParam("订单对象")
                             @RequestBody ConfirmOrderRequest orderRequest,
                             HttpServletResponse httpServletResponse) {
        JsonData jsonData = orderService.confirmOrder(orderRequest);

    }

    /**
     * 查询订单状态
     * <p>
     * 此接口没有登录拦截，可以增加一个秘钥进行rpc通信
     *
     * @param outTradeNo
     * @return
     */
    @ApiOperation("查询订单状态")
    @GetMapping("query_state")
    public JsonData queryProductOrderState(@ApiParam("订单号")
                                           @RequestParam("out_trade_no") String outTradeNo) {

        String state = orderService.queryProductOrderState(outTradeNo);

        return StringUtils.isBlank(state) ? JsonData.buildResult(BizCodeEnum.ORDER_CONFIRM_NOT_EXIST) : JsonData.buildSuccess(state);

    }


}

