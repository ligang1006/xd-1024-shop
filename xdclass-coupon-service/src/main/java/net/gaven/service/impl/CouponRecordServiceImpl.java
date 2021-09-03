package net.gaven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.gaven.config.RabbitMqConfig;
import net.gaven.enums.*;
import net.gaven.exception.BizException;
import net.gaven.feign.ProductOrderFeignService;
import net.gaven.interceptor.LoginInterceptor;
import net.gaven.mapper.CouponRecordMapper;
import net.gaven.mapper.CouponTaskMapper;
import net.gaven.model.CouponRecordDO;
import net.gaven.model.CouponRecordMessage;
import net.gaven.model.CouponTaskDO;
import net.gaven.model.LoginUser;
import net.gaven.request.LockCouponRecordRequest;
import net.gaven.service.ICouponRecordService;
import net.gaven.util.JsonData;
import net.gaven.vo.CouponRecordVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: lee
 * @create: 2021/8/15 5:45 下午
 **/
@Slf4j
@Service
public class CouponRecordServiceImpl implements ICouponRecordService {
    @Resource
    private CouponRecordMapper couponRecordMapper;
    @Resource
    private CouponTaskMapper taskMapper;
    @Autowired
    private RabbitMqConfig rabbitMqConfig;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private CouponTaskMapper couponTaskMapper;
    @Autowired
    private ProductOrderFeignService productOrderFeignService;

    @Override
    public Map<String, Object> page(Integer page, Integer size) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        IPage<CouponRecordDO> couponRecordDOPage = new Page<>(page, size);

        IPage<CouponRecordDO> selectPage = couponRecordMapper.selectPage(couponRecordDOPage, new QueryWrapper<CouponRecordDO>()
                .eq("user_id", loginUser.getId())
                .orderByDesc("create_time"));
        Map<String, Object> result = new HashMap<>(4);
        List<CouponRecordDO> records = selectPage.getRecords();
        long total = selectPage.getTotal();
        long pages = selectPage.getPages();
        result.put("total_record", total);
        result.put("total_page", pages);
        if (!CollectionUtils.isEmpty(records)) {
            result.put("coupon_data", records.stream().map(obj -> convertDO2VOProcess(obj)).collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public CouponRecordVO detail(Long recordId) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        if (loginUser == null) {
            return null;
        }
        CouponRecordDO couponRecordDO = couponRecordMapper.selectOne(new QueryWrapper<CouponRecordDO>()
                .eq("user_id", loginUser.getId())
                .eq("id", recordId));
        if (couponRecordDO == null) {
            return null;
        }
        return convertDO2VOProcess(couponRecordDO);
    }

    private CouponRecordVO convertDO2VOProcess(CouponRecordDO couponRecordDO) {
        CouponRecordVO recordVO = new CouponRecordVO();
        BeanUtils.copyProperties(couponRecordDO, recordVO);
        return recordVO;
    }

    /**
     * 锁定优惠卷
     * 订单号关联优惠卷
     * * 1）锁定优惠券记录   在优惠卷记录表插入
     * * 2）task表插入记录
     * <p>
     * `coupon_record_id` '优惠券记录id',
     * `out_trade_no`'订单号',
     * `lock_state` '锁定状态 锁定LOCK-完成FINISH 取消CANCEL',
     * * 3）发送延迟消息
     *
     * @param recordRequest
     * @return
     */
    @Override
    public JsonData lockCouponRecords(LockCouponRecordRequest recordRequest) {
        //锁定优惠券记录
        List<Long> lockCouponRecordIds = recordRequest.getLockCouponRecordIds();
        String orderNo = recordRequest.getOrderOutTradeNo();
        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        int recordRows = couponRecordMapper.lockUseStateBatch(loginUser.getId(),
                CouponStateEnum.USED.name(), lockCouponRecordIds);

        List<CouponTaskDO> taskDOList = lockCouponRecordIds.stream().map(
                record -> getCouponTaskDO(record, orderNo)).collect(Collectors.toList());
        //task表插入记录
        int taskRows = taskMapper.insertBatch(taskDOList);
        if (lockCouponRecordIds.size() == recordRows
                && recordRows == taskRows) {
            //发送延迟消息,把task记录保存
            for (CouponTaskDO task : taskDOList) {
                CouponRecordMessage recordMessage = new CouponRecordMessage();
                recordMessage.setOutTradeNo(task.getOutTradeNo());
                recordMessage.setTaskId(task.getId());
                log.info("rabbitMq send message start...");
                rabbitTemplate.convertAndSend(rabbitMqConfig.getEventExchange(), rabbitMqConfig.getCouponReleaseDelayRoutingKey(), recordMessage);
                log.info("rabbitMq send message{} end ", recordMessage);
            }

            return JsonData.buildSuccess();
        } else {
            throw new BizException(BizCodeEnum.COUPON_RECORD_LOCK_FAIL);
        }

    }

    private CouponTaskDO getCouponTaskDO(Long recordId, String orderNo) {

        CouponTaskDO couponTaskDO = new CouponTaskDO();
        couponTaskDO.setCouponRecordId(recordId);
        couponTaskDO.setCreateTime(new Date());
        couponTaskDO.setOutTradeNo(orderNo);
        couponTaskDO.setLockState(CouponTaskStatusEnum.LOCK.name());
        return couponTaskDO;
    }

    /**
     * *解锁优惠券记录
     * * 1）查询task工作单是否存在
     * * 2) 查询订单状态
     * <p>
     * 修改task表只有Lock状态才进行修改，可能存在重复的问题
     * <p>
     * 工作单的状态根据 订单状态改变
     * 订单--->new     重新投递
     * 订单--->cancel  释放优惠卷
     * 订单--->PAY     标记优惠卷使用了
     *
     * @param recordMessage
     * @return
     */
    @Override
    public boolean releaseCouponRecord(CouponRecordMessage recordMessage) {
        //查询task是否存在
        CouponTaskDO couponTaskDO = couponTaskMapper.selectById(recordMessage.getTaskId());
        if (couponTaskDO == null) {
            log.warn("工作单不存，消息:{}", recordMessage);
            return true;
        }
        //lock状态才处理
        if (CouponTaskStatusEnum.LOCK.name().equals(couponTaskDO.getLockState())) {
            //查询订单状态,支付了
            JsonData status = productOrderFeignService.getOrderProductStatus(recordMessage.getOutTradeNo());
            if (status.getCode().equals(0)) {
                //订单--->new     重新投递
                //订单--->cancel  释放优惠卷
                //订单--->PAY     标记优惠卷使用了

                if (status.getData().equals(ProductOrderStateEnum.NEW.name())) {
                    //状态是NEW新建状态，则返回给消息队，列重新投递
                    log.warn("订单状态是NEW,返回给消息队列，重新投递:{}", recordMessage);
                    return false;
                } else if (status.getData().equals(ProductOrderStateEnum.PAY.name())) {
                    //状态是PAY建状态，返回，这里优惠卷的状态肯定是在下单的时候修改的
                    log.info("订单已经支付，修改库存锁定工作单FINISH状态:{}", recordMessage);
                    couponTaskDO.setLockState(CouponTaskStatusEnum.FINISH.name());
                    couponTaskMapper.update(couponTaskDO, new QueryWrapper<CouponTaskDO>()
                            .eq("id", recordMessage.getTaskId()));
                    return true;
                }
                //状态是CANCEL建状态，返回
                couponTaskDO.setLockState(CouponTaskStatusEnum.CANCEL.name());
                couponTaskMapper.update(couponTaskDO, new QueryWrapper<CouponTaskDO>()
                        .eq("id", recordMessage.getTaskId()));
                //恢复优惠券记录是NEW状态
                couponRecordMapper.updateStatus(couponTaskDO.getCouponRecordId(),CouponStateEnum.NEW.name());
                log.warn("订单状态是CANCEL,返回给消息队列，重新投递:{}", recordMessage);
                return true;
            } else {
                //查询订单状态失败
                log.warn("查询工单状态失败,state={},消息体={}", couponTaskDO.getLockState(), recordMessage);
                return true;
            }
        } else {
            log.warn("工作单状态不是LOCK,state={},消息体={}", couponTaskDO.getLockState(), recordMessage);
            return true;
        }
    }

}
