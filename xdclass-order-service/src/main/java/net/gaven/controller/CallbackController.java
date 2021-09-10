package net.gaven.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import net.gaven.callback.AlipayCallBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: lee
 * @create: 2021/9/10 9:25 上午
 **/
@Api("订单回调通知模块")
@Controller
@Slf4j
@RequestMapping("/api/callback/order/v1")
public class CallbackController {

    @Autowired
    AlipayCallBackService alipayCallBackService;
    @PostMapping("alipay")
    public String alipayCallback(HttpServletRequest request, HttpServletResponse response){
        //将异步通知中收到的所有参数存储到map中
        Map<String, String> requestParams = convertRequestParamsToMap(request);
        log.info("callback from alipay and param is {}",requestParams);
       return alipayCallBackService.handlerCallBack(requestParams);

    }

    /**
     * 将request中的参数转换成Map
     *
     * @param request
     * @return
     */
    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> paramsMap = new HashMap<>(16);
        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int size = values.length;
            if (size == 1) {
                paramsMap.put(name, values[0]);
            } else {
                paramsMap.put(name, "");
            }
        }
        return paramsMap;
    }


}
