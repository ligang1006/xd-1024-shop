package net.gaven.service;

import net.gaven.model.CouponRecordMessage;
import net.gaven.request.LockCouponRecordRequest;
import net.gaven.util.JsonData;
import net.gaven.vo.CouponRecordVO;

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

    /**
     * 获取详情
     *
     * @param recordId
     * @return
     */
    CouponRecordVO detail(Long recordId);

    /**
     * 锁定优惠券
     *
     * @param recordRequest
     * @return
     */
    JsonData lockCouponRecords(LockCouponRecordRequest recordRequest);

    /**
     * 释放优惠卷
     *
     * @param recordMessage
     * @return
     */
    boolean releaseCouponRecord(CouponRecordMessage recordMessage);

}
