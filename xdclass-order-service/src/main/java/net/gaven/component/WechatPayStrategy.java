package net.gaven.component;

import lombok.extern.slf4j.Slf4j;
import net.gaven.vo.PayInfoVO;
import org.springframework.stereotype.Service;

/**
 * @author: lee
 * @create: 2021/9/8 5:09 下午
 **/
@Slf4j
@Service
public class WechatPayStrategy implements PayStrategy{
    @Override
    public String unifiedorder(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String refund(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String queryPaySuccess(PayInfoVO payInfoVO) {
        return null;
    }
}
