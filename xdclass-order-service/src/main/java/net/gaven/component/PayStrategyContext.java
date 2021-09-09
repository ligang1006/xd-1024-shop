package net.gaven.component;

import net.gaven.vo.PayInfoVO;

/**
 * 策略的上下文，以后统一的方法使用这个上下文
 * <p>
 * 主要就是利用多态，通过上下文的构造，传子类实现，实现相应的功能
 *
 * @author: lee
 * @create: 2021/9/8 5:44 下午
 **/
public class PayStrategyContext {
    private PayStrategy payStrategy;

    public PayStrategyContext(PayStrategy payStrategy) {
        this.payStrategy = payStrategy;
    }
    /*暂时只设置两个通用的方法，下单和退款*/

    /**
     * 根据支付策略，调用不同的支付
     *
     * @param payInfoVO
     * @return
     */
    public String executeUnifiedorder(PayInfoVO payInfoVO) {
        String unifiedorder = payStrategy.unifiedorder(payInfoVO);
        return unifiedorder;
    }

    /**
     * 根据支付的策略，调用不同的查询订单支持状态
     * @param payInfoVO
     * @return
     */
    public String executeQueryPaySuccess(PayInfoVO payInfoVO){
        return this.payStrategy.queryPaySuccess(payInfoVO);

    }
}
