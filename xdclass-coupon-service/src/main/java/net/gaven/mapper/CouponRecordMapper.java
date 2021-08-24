package net.gaven.mapper;

import net.gaven.model.CouponRecordDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lee
 * @since 2021-08-12
 */
public interface CouponRecordMapper extends BaseMapper<CouponRecordDO> {
    /**
     *
     * @return
     */
    int lockUseStateBatch(@Param("userId") Long userId, @Param("useState") String useState, @Param("lockCouponRecordIds") List<Long> lockCouponRecordIds);
}
