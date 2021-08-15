package net.gaven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.gaven.interceptor.LoginInterceptor;
import net.gaven.mapper.CouponRecordMapper;
import net.gaven.model.CouponRecordDO;
import net.gaven.model.LoginUser;
import net.gaven.service.ICouponRecordService;
import net.gaven.vo.CouponRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

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
            result.put("coupon_data", records.stream().map(obj -> getProcess(obj)).collect(Collectors.toList()));
        }
        return result;
    }

    private CouponRecordVO getProcess(CouponRecordDO couponRecordDO) {
        CouponRecordVO recordVO = new CouponRecordVO();
        BeanUtils.copyProperties(couponRecordDO, recordVO);
        return recordVO;
    }
}
