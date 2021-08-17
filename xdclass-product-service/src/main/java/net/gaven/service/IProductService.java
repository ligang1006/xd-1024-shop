package net.gaven.service;

import net.gaven.model.ProductDO;
import net.gaven.util.JsonData;
import net.gaven.vo.ProductVO;

import java.util.List;
import java.util.Map;

/**
 * @author: lee
 * @create: 2021/8/16 9:37 上午
 **/
public interface IProductService {
    /**
     * 获取商品列表
     *
     * @param page
     * @param size
     * @return
     */
    Map<String, Object> getList(Integer page, Integer size);

    /**
     * 获取商品详情
     *
     * @param productId
     * @return
     */
    ProductVO productDetail(Long productId);

    /**
     * 根据Id获取全部商品信息
     *
     * @param ids
     * @return
     */
    List<ProductVO> getAllProductById(List<Long> ids);
}
