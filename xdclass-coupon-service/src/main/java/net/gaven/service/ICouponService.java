package net.gaven.service;



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
}
