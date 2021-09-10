package net.gaven.mapper;

import net.gaven.model.ProductOrderItemDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lee
 * @since 2021-08-17
 */
public interface ProductOrderItemMapper extends BaseMapper<ProductOrderItemDO> {
    /**
     * 批量插入
     *
     * @param list
     */
    void insertBatch(@Param("orderItemList") List<ProductOrderItemDO> list);
}
