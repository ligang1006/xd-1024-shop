package net.gaven.component;

import lombok.extern.slf4j.Slf4j;
import net.gaven.enums.ClientType;
import net.gaven.enums.ProductOrderPayTypeEnum;
import net.gaven.vo.PayInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: lee
 * @create: 2021/9/8 5:51 下午
 **/
@Slf4j
@Component
public class PayFactory {
    @Autowired
    private AlipayStrategy alipayStrategy;
    @Autowired
    private WechatPayStrategy wechatPayStrategy;

    public String pay(PayInfoVO payInfoVO) {
        if (ProductOrderPayTypeEnum.ALIPAY.name().equalsIgnoreCase(payInfoVO.getPayType())) {
            //支付宝支付
            PayStrategyContext payStrategyContext = new PayStrategyContext(alipayStrategy);
//            return alipayStrategy.unifiedorder(payInfoVO); 都是上下文来做
            return payStrategyContext.executeUnifiedorder(payInfoVO);
        } else if (ProductOrderPayTypeEnum.WECHAT.name().equalsIgnoreCase(payInfoVO.getPayType())) {
            PayStrategyContext payStrategyContext = new PayStrategyContext(wechatPayStrategy);

            return payStrategyContext.executeUnifiedorder(payInfoVO);
        }
        return "";
    }

    /**
     * 支付api
     * <link>https://opendocs.alipay.com/apis</link>
     *
     * @param payInfoVO
     * @return
     */
    public String queryPaySuccess(PayInfoVO payInfoVO) {
        if (ProductOrderPayTypeEnum.ALIPAY.name().equalsIgnoreCase(payInfoVO.getPayType())) {
            //支付宝支付
            PayStrategyContext payStrategyContext = new PayStrategyContext(alipayStrategy);
            return payStrategyContext.executeQueryPaySuccess(payInfoVO);
        } else if (ProductOrderPayTypeEnum.WECHAT.name().equalsIgnoreCase(payInfoVO.getPayType())) {
            PayStrategyContext payStrategyContext = new PayStrategyContext(wechatPayStrategy);
            return payStrategyContext.executeQueryPaySuccess(payInfoVO);
        }
        return "";
    }

}
