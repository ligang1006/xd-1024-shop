package net.gaven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.gaven.mapper.BannerMapper;
import net.gaven.model.BannerDO;
import net.gaven.service.IBannerService;
import net.gaven.util.JsonData;
import net.gaven.vo.BannerVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: lee
 * @create: 2021/8/16 9:40 上午
 **/
@Slf4j
@Service
public class BannerServiceImpl implements IBannerService {
    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public JsonData listBanner() {
        List<BannerDO> bannerDOS = bannerMapper.selectList(new QueryWrapper<BannerDO>()
                .orderByAsc("weight"));
        if (CollectionUtils.isEmpty(bannerDOS)) {
            return null;
        } else {
            List<BannerVO> bannerVOList = bannerDOS.stream().map(obj -> convertDO2VO(obj)
            ).collect(Collectors.toList());
            return JsonData.buildSuccess(bannerVOList);
        }
    }

    private BannerVO convertDO2VO(BannerDO obj) {
        BannerVO bannerVO = new BannerVO();
        BeanUtils.copyProperties(obj, bannerVO);
        return bannerVO;
    }
}
