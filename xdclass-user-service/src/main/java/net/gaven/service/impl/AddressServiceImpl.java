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

import net.gaven.vo.AddressVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author: lee
 * @create: 2021/8/3 3:31 下午
 **/
@Slf4j
@Service
public class AddressServiceImpl implements IAddressService {
    @Resource
    private AddressMapper addressMapper;

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


    @Override
    public AddressDO getAddressById(Integer id) {
        AddressDO addressDO = addressMapper.selectById(id);
        return addressDO;
    }


    /**
     * 通过Id获取收货地址
     * <p>
     * 注意越权问题
     *
     * @param addressId
     * @return
     */
    @Override
    public AddressVO getAddressDetail(int addressId) {

        if (StringUtils.isEmpty(addressId)) {
            return null;
        }
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        if (loginUser == null) {
            return null;
        }
        AddressDO addressDO = addressMapper
                .selectOne(new QueryWrapper<AddressDO>()
                        .eq("id", addressId)
                        .eq("user_id", loginUser.getId()));

        if (addressDO != null) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressDO, addressVO);
            return addressVO;
        } else {
            return null;
        }
    }

    /**
     * 获取列表信息
     * 只有有转化的就想到 java8的map A--->B有对象的转化
     *
     * @return
     */
    @Override
    public List<AddressVO> listAddress() {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        Long userId = loginUser.getId();
        if (userId != null) {
            List<AddressDO> addressDOS = addressMapper.selectList(new QueryWrapper<AddressDO>().eq("user_id", userId));
            if (!CollectionUtils.isEmpty(addressDOS)) {
                List<AddressVO> collect = addressDOS.stream().map(obj -> {
                    AddressVO addressVO = new AddressVO();
                    BeanUtils.copyProperties(obj, addressVO);
                    return addressVO;
                }).collect(Collectors.toList());
                return collect;
            }
            return null;
        }
        return null;
    }

    /**
     * TODO 注意，这里查询不到时删除会有问题吗？
     * 没问题
     * 注意越权问题
     *
     * @param addressId
     * @return
     */
    @Override
    public int deleteAddress(int addressId) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        if (loginUser == null) {
            return 0;
        }
        int rows = addressMapper
                .delete(new QueryWrapper<AddressDO>()
                        .eq("id", addressId)
                        .eq("user_id", loginUser.getId()));
        return rows;
    }
}
