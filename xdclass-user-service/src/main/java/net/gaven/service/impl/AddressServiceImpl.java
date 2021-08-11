package net.gaven.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.gaven.enums.DefaultAddressStatusEnum;
import net.gaven.interceptor.LoginInterceptor;
import net.gaven.mapper.AddressMapper;
import net.gaven.model.AddressDO;
import net.gaven.model.LoginUser;
import net.gaven.request.AddressAddRequest;
import net.gaven.service.IAddressService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @author: lee
 * @create: 2021/8/3 3:31 下午
 **/
@Slf4j
@Service
public class AddressServiceImpl implements IAddressService {
    @Resource
    private AddressMapper addressMapper;


    @Override
    public AddressDO getAddressById(Integer id) {
        AddressDO addressDO = addressMapper.selectById(id);
        return addressDO;
    }

    /**
     * 1、判断是否有过默认地址
     * 如果有--将之前的默认修改
     * 如果没有 增加
     *
     * @param addressAddRequest
     */
    @Override
    public void addAddress(AddressAddRequest addressAddRequest) {
        if (addressAddRequest == null) {
            return;
        }
        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        AddressDO defaultAddressDO = addressMapper.selectOne(new QueryWrapper<AddressDO>()
                .eq("default_status", DefaultAddressStatusEnum.DEFAULT_ADDRESS.getCode())
                .eq("user_id", loginUser.getId()));
        if (defaultAddressDO != null) {
            //之前有默认地址，修改默认地址
            defaultAddressDO.setDefaultStatus(DefaultAddressStatusEnum.COMMON_ADDRESS.getCode());
            addressMapper.update(defaultAddressDO, new QueryWrapper<AddressDO>().eq("id", defaultAddressDO.getId()));
        }
        saveAddressDo(addressAddRequest, loginUser.getId());


    }

    private void saveAddressDo(AddressAddRequest addressAddRequest, Long id) {
        AddressDO addressDO = new AddressDO();
        BeanUtils.copyProperties(addressAddRequest, addressDO);
        addressDO.setUserId(id);
        addressDO.setCreateTime(new Date());
        int rows = addressMapper.insert(addressDO);
        log.info("add address rows:{},data{}", rows, addressDO);
    }
}
