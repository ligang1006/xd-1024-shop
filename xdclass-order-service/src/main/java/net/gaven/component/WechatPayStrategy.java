package net.gaven.component;

import net.gaven.vo.PayInfoVO;

/**
 * @author: lee
 * @create: 2021/9/8 5:09 下午
 **/
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
