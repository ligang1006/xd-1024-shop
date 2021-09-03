package net.gaven.mapper;

import net.gaven.model.CouponRecordDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lee
 * @since 2021-08-12
 */
public interface CouponRecordMapper extends BaseMapper<CouponRecordDO> {
    /**
     * 批量锁定用户状态
     *
     * @param userId
     * @param useState
     * @param lockCouponRecordIds
     * @return
     */
    int lockUseStateBatch(@Param("userId") Long userId, @Param("useState") String useState, @Param("lockCouponRecordIds") List<Long> lockCouponRecordIds);

    /**
     * 更新优惠券状态
     *
     * @param couponRecordId
     * @param status
     * @return 更新条数
     */
    int updateStatus(@Param("couponRecordId") Long couponRecordId, @Param("status") String status);
}
