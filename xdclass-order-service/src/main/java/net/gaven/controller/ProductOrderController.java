package net.gaven.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.gaven.service.IOrderService;
import net.gaven.util.JsonData;
import net.gaven.vo.ConfirmOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

}

