package net.gaven.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import net.gaven.model.AddressDO;
import net.gaven.service.IAddressService;
import net.gaven.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 电商-公司收发货地址表 前端控制器
 * </p>
 *
 * @author 二当家小D
 * @since 2021-08-03
 */
@Api(tags = "收货地址模块")
@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    @Autowired
    private IAddressService addressService;

    @ApiOperation("根据id查询详情")
    @GetMapping("/find/{address_id}")
    public JsonData getAddressById(@ApiParam(value = "地址Id", required = true)
                                   @PathVariable("address_id") Integer addressId) {
        AddressDO addressDO = addressService.getAddressById(addressId);
        return JsonData.buildSuccess(addressDO);
    }

}

