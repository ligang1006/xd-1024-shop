package net.gaven.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.gaven.service.IBannerService;
import net.gaven.util.JsonData;
import net.gaven.vo.BannerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lee
 * @since 2021-08-16
 */
@Api("轮播图模块")
@RestController
@RequestMapping("/api/banner/v1")
public class BannerController {
    @Autowired
    private IBannerService bannerService;

    @ApiOperation("获取轮播图列表")
    @GetMapping("/list")
    public JsonData getBannerList() {
        return bannerService.listBanner();
    }

}

