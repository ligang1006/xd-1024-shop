package net.gaven.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import net.gaven.enums.BizCodeEnum;
import net.gaven.exception.CustomException;
import net.gaven.model.AddressDO;
import net.gaven.request.AddressAddRequest;
import net.gaven.service.IAddressService;
import net.gaven.util.JsonData;
import net.gaven.vo.AddressVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    //http://localhost:9001/api/v1/address/find/1
    @GetMapping("/find/{address_id}")
    public JsonData getAddressById(@ApiParam(value = "地址Id", required = true)
                                   @PathVariable("address_id") Integer addressId) {
        AddressDO addressDO = addressService.getAddressById(addressId);
        if (addressDO.getId() == 1) {
            throw new CustomException(BizCodeEnum.OPS_REPEAT.getCode(), "数据重复");

        } else {
            int i = 10 / 0;

        }
        return JsonData.buildSuccess(addressDO);
    }

    /**
     * 新增收货地址
     *
     * @param addressAddRequest
     * @return
     */
    @ApiOperation("新增收货地址")
    @PostMapping("/add")
    public JsonData addAddress(@RequestBody AddressAddRequest addressAddRequest) {
        addressService.addAddress(addressAddRequest);
        return JsonData.buildSuccess("add address success");
    }

    /**
     * 获取收货地址
     *
     * @param addressId
     * @return
     */
    @ApiOperation("获取收货地址")
    @PostMapping("/detail")
    public JsonData addressDetail(@ApiParam("查询的地址Id")
                                  @RequestParam(value = "address_id") int addressId) {
        AddressVO addressVO = addressService.getAddressDetail(addressId);
        return addressVO == null ? JsonData.buildResult(BizCodeEnum.ADDRESS_NO_EXITS) : JsonData.buildSuccess(addressVO);
    }

    /**
     * 删除收货地址
     *
     * @param addressId
     * @return
     */
    @ApiOperation("删除收货地址")
    @PostMapping("/delete")
    public JsonData addressDelete(@ApiParam("删除的地址Id")
                                  @RequestParam(value = "address_id") int addressId) {
        int rows = addressService.deleteAddress(addressId);
        return rows == 1 ? JsonData.buildSuccess() : JsonData.buildResult(BizCodeEnum.ADDRESS_DEL_FAIL);
    }

}

