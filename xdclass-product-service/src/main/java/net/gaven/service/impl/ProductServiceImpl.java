package net.gaven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.gaven.mapper.ProductMapper;
import net.gaven.model.ProductDO;
import net.gaven.service.IProductService;
import net.gaven.util.JsonData;
import net.gaven.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: lee
 * @create: 2021/8/16 9:38 上午
 **/
@Slf4j
@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    /**
     * @return
     */
    @Override
    public Map<String, Object> getList(Integer page, Integer size) {
        Page<ProductDO> productDOPage = new Page<>(page, size);

        Page<ProductDO> pageProducts = productMapper.selectPage(productDOPage, new QueryWrapper<ProductDO>()
                .orderByDesc("create_time"));
        Map<String, Object> result = new HashMap<>(4);
        result.put("total_page", pageProducts.getPages());
        result.put("total_record", pageProducts.getTotal());
        if (!CollectionUtils.isEmpty(pageProducts.getRecords())) {
            result.put("record", pageProducts.getRecords().stream().map(obj -> getProductVO(obj)).collect(Collectors.toList()));
        }
        return result;
    }

    private ProductVO getProductVO(ProductDO obj) {
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(obj, productVO);
        return productVO;
    }
}
