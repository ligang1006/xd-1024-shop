package net.gaven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.gaven.enums.BizCodeEnum;
import net.gaven.enums.ProductTaskStatusEnum;
import net.gaven.exception.BizException;
import net.gaven.mapper.ProductMapper;
import net.gaven.mapper.ProductTaskMapper;
import net.gaven.model.ProductDO;
import net.gaven.model.ProductTaskDO;
import net.gaven.request.LockProductRequest;
import net.gaven.request.OrderItemRequest;
import net.gaven.service.IProductService;
import net.gaven.util.JsonData;
import net.gaven.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    @Autowired
    private ProductTaskMapper productTaskMapper;

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

    @Override
    public ProductVO productDetail(Long productId) {
        ProductDO productDO = productMapper.selectOne(
                new QueryWrapper<ProductDO>().eq("id", productId));
        if (productDO != null) {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(productDO, productVO);
            return productVO;
        }
        return null;
    }

    /**
     * 根据Id查询商品信息
     *
     * @param ids
     * @return
     */
    @Override
    public List<ProductVO> getAllProductById(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }

//        productMapper.selectList(new QueryWrapper<ProductDO>().in("id", ids));
        List<ProductDO> productDOS = productMapper.selectBatchIds(ids);
        if (CollectionUtils.isEmpty(productDOS)) {
            return null;
        }
        return productDOS.stream().map(obj -> getProductVO(obj)).collect(Collectors.toList());
    }

    /**
     * 锁定商品，数量
     * 1、查询出要锁定的商品
     * 2、校验是否有足够的库存
     * 3、锁定task表
     *
     * @param lockProductRequest
     * @return
     */
    @Override
    public JsonData lockProductStack(LockProductRequest lockProductRequest) {
        //批量查询商品
        List<OrderItemRequest> orderItemList = lockProductRequest.getOrderItemList();
        if (CollectionUtils.isEmpty(orderItemList)) {
            return null;
        }
        List<Long> productIds = orderItemList.stream()
                .map(OrderItemRequest::getProductId).collect(Collectors.toList());

        List<ProductVO> productVOS = this.getAllProductById(productIds);
        Map<Long, ProductVO> productsInfo =
                productVOS.stream().collect(Collectors.toMap(ProductVO::getId, Function.identity()));
        String orderOutTradeNo = lockProductRequest.getOrderOutTradeNo();

        //更新商品状态
        updateProductTask(productsInfo, orderOutTradeNo, orderItemList);


        return JsonData.buildSuccess();
    }

    private void updateProductTask(
            Map<Long, ProductVO> productsInfo, String orderOutTradeNo, List<OrderItemRequest> orderItems) {

        for (OrderItemRequest item : orderItems) {
            //锁定商品记录
            int rows = productMapper.lockProductStock(item.getProductId(), item.getBuyNum());
            if (rows != 1) {
                throw new BizException(BizCodeEnum.PRODUCT_LOCK_FAIL);
            }
            //存储task表
            ProductTaskDO productTaskDO = new ProductTaskDO();
            productTaskDO.setProductId(item.getProductId());
            productTaskDO.setBuyNum(item.getBuyNum());
            productTaskDO.setLockState(ProductTaskStatusEnum.LOCK.name());
            productTaskDO.setOutTradeNo(orderOutTradeNo);
            productTaskDO.setProductName(productsInfo.get(item.getProductId()).getTitle());
            productTaskDO.setCreateTime(new Date());
            productTaskMapper.insert(productTaskDO);
            log.info("商品库存锁定-插入商品product_task成功:{}", productTaskDO);

            //TODO 发送MQ延迟消息，介绍商品库存
        }
    }
}
