package net.gaven.mapper;

import net.gaven.model.ProductDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.gaven.model.ProductTaskDO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lee
 * @since 2021-08-16
 */
public interface ProductMapper extends BaseMapper<ProductDO> {
    /**
     * 锁定商品
     *
     * @param productId
     * @param buyNum
     * @return
     */
    int lockProductStock(@Param("productId") Long productId, @Param("buyNum") Integer buyNum);

    /**
     * 恢复商品的数量
     *
     * @param productId
     * @param buyNum
     */
    void unlockProductStock(@Param("productId") Long productId, @Param("buyNum") Integer buyNum);
}
