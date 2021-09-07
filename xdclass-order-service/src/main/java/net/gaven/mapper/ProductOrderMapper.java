package net.gaven.mapper;

import net.gaven.model.ProductOrderDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lee
 * @since 2021-08-17
 */
public interface ProductOrderMapper extends BaseMapper<ProductOrderDO> {
    /**
     * 更新订单状态
     *
     * @param newState
     * @param oldState
     * @return
     */
    int updateOrderPayState(@Param("newState") String newState,
                            @Param("oldState") String oldState,
                            @Param("outTradeNo") String outTradeNo);
}
