package net.gaven.component;

import net.gaven.vo.PayInfoVO;

/**
 * 支付的策略
 *
 * @author: lee
 * @create: 2021/9/8 4:46 下午
 **/
public interface PayStrategy {
    /**
     * 下单
     * @return
     */
    String unifiedorder(PayInfoVO payInfoVO);


    /**
     *  退款
     * @param payInfoVO
     * @return
     */
    default String refund(PayInfoVO payInfoVO){return "";}


    /**
     * 查询支付是否成功
     * @param payInfoVO
     * @return
     */
    default String queryPaySuccess(PayInfoVO payInfoVO){return "";}


}
