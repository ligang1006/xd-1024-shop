package net.gaven.service;

import net.gaven.model.AddressDO;
import net.gaven.request.AddressAddRequest;

/**
 * <p>
 * 电商-公司收发货地址表 服务类
 * </p>
 *
 * @author 二当家小D
 * @since 2021-08-03
 */
public interface IAddressService {
    /**
     * 根据Id查询详情
     *
     * @param id
     * @return
     */
    AddressDO getAddressById(Integer id);

    /**
     * 增加收货地址
     *
     * @param addressAddRequest
     */
    void addAddress(AddressAddRequest addressAddRequest);
}
