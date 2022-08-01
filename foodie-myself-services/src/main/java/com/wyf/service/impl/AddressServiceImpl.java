package com.wyf.service.impl;

import com.wyf.mapper.UserAddressMapper;
import com.wyf.popj.UserAddress;
import com.wyf.popj.bo.AddressBO;
import com.wyf.service.AddressService;
import com.wyf.utils.RandomId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    // 根据userId 查询地址
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<UserAddress> selectAddressByUserId(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);

        List<UserAddress> userAddressList = userAddressMapper.select(userAddress);

        return userAddressList;
    }

    // 新增地址
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean addAddress(AddressBO addressBO) {
        Integer isDefault = 0;
        // 1.检查表中是否有其他地址 如果 没有设置该地址为默认地址
        List<UserAddress> addressList = selectAddressByUserId(addressBO.getUserId());

        if (addressList == null || addressList.isEmpty() || addressList.size() == 0) {
            isDefault = 1;
        }

        // 2.调整数据
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, userAddress);
        userAddress.setId(RandomId.generatorId());
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());

        // 3.插入数据
        Integer result = userAddressMapper.insert(userAddress);

        return result == 0 ? false : true;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean updateUserAddress(AddressBO addressBO) {
        UserAddress userAddress = new UserAddress();

        BeanUtils.copyProperties(addressBO, userAddress);
        userAddress.setId(addressBO.getAddressId());
        userAddress.setUpdatedTime(new Date());

        Integer result = userAddressMapper.updateByPrimaryKeySelective(userAddress);

        return result != 0 ? true : false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean deleteUserAddress(String userId, String addressId) {
        UserAddress address = new UserAddress();
        address.setId(addressId);
        address.setUserId(userId);

        Integer result = userAddressMapper.delete(address);
        return result != 0 ? true : false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean updateUserAddressToBeDefault(String userId, String addressId) {
        // 1. 查找默认地址，设置为不默认
        UserAddress queryAddress = new UserAddress();
        queryAddress.setUserId(userId);
        queryAddress.setIsDefault(1);
        List<UserAddress> list = userAddressMapper.select(queryAddress);

        for (UserAddress ua : list) {
            ua.setIsDefault(0);
            userAddressMapper.updateByPrimaryKeySelective(ua);
        }


        // 2. 根据地址id修改为默认的地址
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setId(addressId);
        defaultAddress.setUserId(userId);
        defaultAddress.setIsDefault(1);
        Integer result = userAddressMapper.updateByPrimaryKeySelective(defaultAddress);

        return result != 0 ? true : false;
    }
}
