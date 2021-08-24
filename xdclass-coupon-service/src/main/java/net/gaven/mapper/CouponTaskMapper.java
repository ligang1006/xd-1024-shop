package net.gaven.mapper;

import net.gaven.model.CouponTaskDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * task表 Mapper 接口
 * </p>
 *
 * @author lee
 * @since 2021-08-22
 */
public interface CouponTaskMapper extends BaseMapper<CouponTaskDO> {

    /**
     * 批量插入
     * @param couponTaskDOList
     * @return
     */
    int insertBatch(@Param("couponTaskList") List<CouponTaskDO> couponTaskDOList);

}
