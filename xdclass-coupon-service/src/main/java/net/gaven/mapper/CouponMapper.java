package net.gaven.mapper;

import net.gaven.model.CouponDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lee
 * @since 2021-08-12
 */
public interface CouponMapper extends BaseMapper<CouponDO> {
    /**
     * 扣减库存数量
     *
     * @param couponId
     * @return
     */
    int reduce(Long couponId);
}
