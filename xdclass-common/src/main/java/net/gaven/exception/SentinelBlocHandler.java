package net.gaven.exception;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import net.gaven.enums.BizCodeEnum;
import net.gaven.util.CommonUtil;
import net.gaven.util.JsonData;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: lee
 * @create: 2021/9/30 9:20 上午
 **/
@Component
public class SentinelBlocHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        JsonData jsonData = null;
        if (e instanceof FlowException) {
            jsonData = JsonData.buildResult(BizCodeEnum.CONTROL_FLOW);
        } else if (e instanceof DegradeException) {
            jsonData = JsonData.buildResult(BizCodeEnum.CONTROL_DEGRADE);

        } else if (e instanceof AuthorityException) {
            jsonData = JsonData.buildResult(BizCodeEnum.CONTROL_AUTH);
        }
        httpServletResponse.setStatus(200);
        CommonUtil.sendJsonMessage(httpServletResponse, jsonData);
    }
}
