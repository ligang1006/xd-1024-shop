package net.gaven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.lang.Collections;
import net.gaven.enums.CouponCategoryEnum;
import net.gaven.enums.CouponPublishEnum;
import net.gaven.mapper.CouponMapper;
import net.gaven.model.CouponDO;
import net.gaven.service.ICouponService;
import net.gaven.vo.CouponVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: lee
 * @create: 2021/8/12 10:27 上午
 **/
@Service
public class CouponService implements ICouponService {
    @Resource
    private CouponMapper couponMapper;

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
}
