package net.gaven.controller;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.gaven.config.AlipayConfig;
import net.gaven.config.PayUrlConfig;
import net.gaven.enums.BizCodeEnum;
import net.gaven.service.IOrderService;
import net.gaven.util.JsonData;
import net.gaven.vo.ConfirmOrderRequest;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lee
 * @since 2021-08-17
 */
@Slf4j
@RestController
@RequestMapping("/api/order/v1")
public class ProductOrderController {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private PayUrlConfig payUrlConfig;

    @ApiOperation("提交订单")
    @PostMapping("/confirm")
    public JsonData confirmOrder(@ApiParam("订单对象")
                                 @RequestBody ConfirmOrderRequest orderRequest,
                                 HttpServletResponse httpServletResponse) {
        JsonData jsonData = orderService.confirmOrder(orderRequest);
        return jsonData;
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

    /**
     * 测试支付方法
     */
    @GetMapping("/test_pay")
    public void testAlipay(HttpServletResponse response) throws AlipayApiException, IOException {

        HashMap<String, String> content = new HashMap<>();
        //商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
        String no = UUID.randomUUID().toString();

        log.info("订单号:{}", no);
        content.put("out_trade_no", no);

        content.put("product_code", "FAST_INSTANT_TRADE_PAY");

        //订单总金额，单位为元，精确到小数点后两位
        content.put("total_amount", String.valueOf("111.99"));

        //商品标题/交易标题/订单标题/订单关键字等。 注意：不可使用特殊字符，如 /，=，&amp; 等。
        content.put("subject", "杯子");

        //商品描述，可空
        content.put("body", "好的杯子");

        // 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
        content.put("timeout_express", "5m");


        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setBizContent(JSON.toJSONString(content));
        request.setNotifyUrl(payUrlConfig.getAlipayCallbackUrl());
        request.setReturnUrl(payUrlConfig.getAlipaySuccessReturnUrl());

        AlipayTradeWapPayResponse alipayResponse = AlipayConfig.getInstance().pageExecute(request);

        if (alipayResponse.isSuccess()) {
            System.out.println("调用成功");

            String form = alipayResponse.getBody();

            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(form);
            response.getWriter().flush();
            response.getWriter().close();

        } else {
            System.out.println("调用失败");
        }
    }

}

