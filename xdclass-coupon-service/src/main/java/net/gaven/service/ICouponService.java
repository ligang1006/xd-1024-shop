package net.gaven.service;


import net.gaven.enums.CouponCategoryEnum;
import net.gaven.util.JsonData;

import java.util.Map;

/**
 * @author: lee
 * @create: 2021/8/12 9:15 上午
 **/
public interface ICouponService {
    /**
     * 获取优惠卷列表，支持分页
     *
     * @param page
     * @param size
     * @return
     */
    Map<String, Object> pageCouponActivity(Integer page, Integer size);

    /**
     * 获取优惠卷
     *
     * @param couponId
     * @param promotion
     * @return
     */
    JsonData addCoupon(Long couponId, CouponCategoryEnum promotion);
}
