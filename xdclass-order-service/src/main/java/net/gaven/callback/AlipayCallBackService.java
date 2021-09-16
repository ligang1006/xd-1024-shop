package net.gaven.callback;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.extern.slf4j.Slf4j;
import net.gaven.config.AlipayConfig;
import net.gaven.enums.ProductOrderPayTypeEnum;
import net.gaven.enums.RequestStatusEnum;
import net.gaven.service.IOrderService;
import net.gaven.util.JsonData;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static net.gaven.constant.Constant.ALIPAY_FAIL;
import static net.gaven.constant.Constant.ALIPAY_SUCCESS;

/**
 * @author: lee
 * @create: 2021/9/10 9:09 上午
 **/
@Slf4j
@Service
public class AlipayCallBackService {
    @Autowired
    private IOrderService orderService;

    public String handlerCallBack(Map<String, String> requestParams) {
        //调用SDK验证签名
        try {
            boolean rsaCertCheckV1 = AlipaySignature.rsaCertCheckV1(requestParams, AlipayConfig.ALIPAY_PUB_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGN_TYPE);
            log.info("check callback rsa params{}",  requestParams);
            if (rsaCertCheckV1) {
                //处理回调数据
                JsonData jsonData = orderService.handlerOrderCallbackMsg(ProductOrderPayTypeEnum.ALIPAY.name(), requestParams);
                if (RequestStatusEnum.OK.getCode().equals(jsonData.getCode())) {
                    //通知结果确认成功，不然会一直通知，八次都没返回success就认为交易失败
                    return ALIPAY_SUCCESS;
                }
            }
        } catch (AlipayApiException e) {
            log.error("check callback rsa fail exception {},params{}", e, requestParams);
        }
        return ALIPAY_FAIL;
    }
}
