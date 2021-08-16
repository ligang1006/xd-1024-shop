package net.gaven.service;

import net.gaven.model.BannerDO;
import net.gaven.util.JsonData;
import net.gaven.vo.BannerVO;

import java.util.List;

/**
 * @author: lee
 * @create: 2021/8/16 9:40 上午
 **/
public interface IBannerService {
    /**
     * 获取轮播图列表
     *
     * @return
     */
    JsonData listBanner();
}
