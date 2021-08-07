package net.gaven.service.impl;


import net.gaven.mapper.AddressMapper;
import net.gaven.model.AddressDO;
import net.gaven.service.IAddressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * @author: lee
 * @create: 2021/8/3 3:31 下午
 **/
@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private AddressMapper addressMapper;


    @Override
    public AddressDO getAddressById(Integer id) {
        AddressDO addressDO = addressMapper.selectById(id);
        return addressDO;
    }
}
