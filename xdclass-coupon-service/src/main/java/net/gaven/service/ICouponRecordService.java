package net.gaven.service;

import java.util.Map;

/**
 * @author: lee
 * @create: 2021/8/15 5:44 下午
 **/
public interface ICouponRecordService {
    /**
     * 获取优惠卷记录列表
     *
     * @param page
     * @param size
     * @return
     */
    Map<String, Object> page(Integer page, Integer size);
}
