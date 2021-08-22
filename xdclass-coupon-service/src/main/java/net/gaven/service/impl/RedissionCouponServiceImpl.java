package net.gaven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.gaven.enums.BizCodeEnum;
import net.gaven.enums.CouponCategoryEnum;
import net.gaven.enums.CouponPublishEnum;
import net.gaven.enums.CouponStateEnum;
import net.gaven.exception.BizException;
import net.gaven.interceptor.LoginInterceptor;
import net.gaven.mapper.CouponMapper;
import net.gaven.mapper.CouponRecordMapper;
import net.gaven.model.CouponDO;
import net.gaven.model.CouponRecordDO;
import net.gaven.model.LoginUser;
import net.gaven.request.NewUserCouponRequest;
import net.gaven.service.ICouponService;
import net.gaven.util.JsonData;
import net.gaven.util.MyRedisTemplate;
import net.gaven.util.MyRedissionClient;
import net.gaven.util.RandomUtil;
import net.gaven.vo.CouponVO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: lee
 * @create: 2021/8/12 10:27 上午
 **/
@Slf4j
@Primary
@Service("redission")
public class RedissionCouponServiceImpl implements ICouponService {
    @Resource
    private CouponMapper couponMapper;
    @Resource
    private CouponRecordMapper couponRecordMapper;
    @Resource
    private MyRedisTemplate myRedisTemplate;
    @Autowired
    private RedissonClient redissioClient;

    @Override
    public Map<String, Object> pageCouponActivity(Integer page, Integer size) {

        Page<CouponDO> couponDOPage = new Page<>(page, size);
        IPage<CouponDO> selectPage = couponMapper.selectPage(couponDOPage, new QueryWrapper<CouponDO>()
                .eq("publish", CouponPublishEnum.PUBLISH)
                .eq("category", CouponCategoryEnum.PROMOTION)
                .orderByDesc("create_time"));

        Map<String, Object> pageMap = new HashMap<>(3);
        pageMap.put("total_page", selectPage.getPages());
        pageMap.put("total_record", selectPage.getTotal());
        if (!CollectionUtils.isEmpty(selectPage.getRecords())) {
            pageMap.put("record", selectPage.getRecords().stream().map(obj -> getCouponVO(obj)).collect(Collectors.toList()));
        }
        return pageMap;
    }

    private CouponVO getCouponVO(CouponDO couponDO) {
        if (couponDO != null) {
            CouponVO couponVO = new CouponVO();
            BeanUtils.copyProperties(couponDO, couponVO);
            return couponVO;
        }
        return null;
    }

    /**
     * 1、校验优惠卷是否存在
     * 2、校验优惠卷是否能够领取，时间、库存、类型
     * 3、扣减优惠卷
     * 4、存库
     * try {
     * //加锁成功,其他线程进入会阻塞等待
     * lock = redissioClient.getLock(lockKey);
     * //有看门狗，默认30s 每10s钟会检查一下是否过期，如果快过期了会自动续期
     * lock.lock();
     * //没有watch dog
     * //            lock.lock(10,TimeUnit.SECONDS);
     * log.info("add lock success key:{} threadId:{}", lockKey, Thread.currentThread().getId());
     * //扣费逻辑
     * //这里有问题，没有被抛出
     * getCoupon(couponId, category, loginUser);
     * } catch (Exception e) {
     * log.error("获取优惠卷发生异常{}", e);
     * } finally {
     * lock.unlock();
     * log.info("成功解锁 当前线程Id:{}", Thread.currentThread().getId());
     * }
     * <p>
     * 注意 ⚠️    try catch 之后，如果问题抛不出去，事务不执行
     *
     * @param couponId
     * @param category
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonData addCoupon(Long couponId, CouponCategoryEnum category) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        String lockKey = "coupon:" + couponId;
        RLock lock = null;
        try {
            //加锁成功,其他线程进入会阻塞等待
            lock = redissioClient.getLock(lockKey);
            //有看门狗，默认30s 每10s钟会检查一下是否过期，如果快过期了会自动续期
            lock.lock();
            //没有watch dog
//            lock.lock(10,TimeUnit.SECONDS);
            log.info("add lock success key:{} threadId:{}", lockKey, Thread.currentThread().getId());
            //扣费逻辑
            getCoupon(couponId, category, loginUser);
        }
        /*注意： ⚠️这里不能catch否则事务抛不出去*/
//        catch (Exception e) {
//            log.error("获取优惠卷发生异常{}", e);
//        }
        finally {
            lock.unlock();
            log.info("成功解锁 当前线程Id:{}", Thread.currentThread().getId());
        }
        return JsonData.buildSuccess("get coupon success");
    }


    //        @Transactional(propagation = Propagation.NEVER)
    void getCoupon(Long couponId, CouponCategoryEnum category, LoginUser loginUser) {

        //校验校验优惠卷是否存在
        CouponDO couponDO = couponMapper.selectOne(new QueryWrapper<CouponDO>()
                .eq("id", couponId)
                .eq("category", category));

        //校验优惠卷是否能够领取，时间、库存、类型
        if (loginUser == null) {
            loginUser = new LoginUser();
            loginUser.setId(Long.getLong(RandomUtil.getRandomNum(2)));
        }
        checkCoupon(couponDO, loginUser.getId());

        CouponRecordDO couponRecordDO = new CouponRecordDO();
        BeanUtils.copyProperties(couponDO, couponRecordDO);
        couponRecordDO.setCreateTime(new Date());
        couponRecordDO.setUseState(CouponStateEnum.NEW.name());
        couponRecordDO.setUserId(loginUser.getId());
        couponRecordDO.setUserName(loginUser.getName());
        couponRecordDO.setCouponId(couponId);
        couponRecordDO.setId(null);
        //扣减优惠卷库存
        int reduceNum = couponMapper.reduceStock(couponId);
//        int reduceNum = 1;
        //存库
        //事务验证
//        int flag = 1 / 0;
        if (reduceNum == 1) {
            reduceCoupon(couponRecordDO);
        } else {
            log.error("reduce coupon fail couponId={},userId={}", couponId, loginUser.getId());
        }
    }

    private void reduceCoupon(CouponRecordDO couponRecordDO) {
        couponRecordMapper.insert(couponRecordDO);
    }

    private void checkCoupon(CouponDO couponDO, Long userId) {
        if (couponDO == null) {
            throw new BizException(BizCodeEnum.COUPON_NO_EXITS);
        }
        //校验优惠卷是否能够领取，时间、库存、类型、
        //库存
        if (couponDO.getStock() < 1) {
            throw new BizException(BizCodeEnum.COUPON_NO_STOCK);
        }
        //时间
        long start = couponDO.getStartTime().getTime();
        long end = couponDO.getEndTime().getTime();
//        long now = new Date().getTime();
        long now = System.currentTimeMillis();
        if (now > end || now < start) {
            throw new BizException(BizCodeEnum.COUPON_OUT_OF_TIME);
        }
        //类型
        if (!CouponPublishEnum.PUBLISH.name().equals(couponDO.getPublish())) {
            throw new BizException(BizCodeEnum.COUPON_CONDITION_ERROR);
        }
        //是否超过次数
        Integer selectNum = couponRecordMapper
                .selectCount(new QueryWrapper<CouponRecordDO>()
                        .eq("coupon_id", couponDO.getId())
                        .eq("user_id", userId));
        if (selectNum > couponDO.getUserLimit()) {
            throw new BizException(BizCodeEnum.COUPON_OUT_OF_LIMIT);
        }
    }

    /**
     * 新人发放优惠卷
     * 1、事务回滚问题？？？记录日志，之后补发
     * 2、用户那时还没登录，生成不了token，所以不能拦截当前接口
     * 3、本地直接调用发放优惠券的方法，需要构造一个登录用户存储在threadLocal
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonData getNewUserCoupon(NewUserCouponRequest request) {
        //查询新人的优惠卷
        List<CouponDO> initCoupons = initNewUserCoupon(request.getUserId(), CouponCategoryEnum.NEW_USER);
        if (CollectionUtils.isEmpty(initCoupons)) {
            return JsonData.buildResult(BizCodeEnum.COUPON_GET_FAIL);
        }
        //设置ThreadLocal
        LoginUser loginUser = new LoginUser();
        loginUser.setId(request.getUserId());
        loginUser.setName(request.getName());
        LoginInterceptor.threadLocal.set(loginUser);

        initCoupons.forEach(couponDO -> {
            this.addCoupon(couponDO.getId(), CouponCategoryEnum.NEW_USER);
        });

        return JsonData.buildSuccess("发送优惠卷成功");
    }

    private List<CouponDO> initNewUserCoupon(long userId, CouponCategoryEnum categoryEnum) {
        if (StringUtils.isEmpty(userId) || categoryEnum == null) {
            return null;
        } else {
            //校验是不是新人 TODO

            List<CouponDO> newCoupons = couponMapper.selectList(new QueryWrapper<CouponDO>().
                    eq("category", categoryEnum.name()));
            return newCoupons;
        }
    }
}
