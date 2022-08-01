package com.wyf.service;

import com.wyf.popj.UserAddress;
import com.wyf.popj.bo.AddressBO;

import java.util.List;

public interface AddressService {

    /**
     * 根据userId 查询地址
     * @return
     * @param userId
     */
    List<UserAddress> selectAddressByUserId(String userId);

    /**
     *
     * 新增地址
     * @param addressBO
     * @return
     */
    Boolean addAddress(AddressBO addressBO);

    /**
     * 修改地址
     * @param addressBO
     * @return
     */
    Boolean updateUserAddress(AddressBO addressBO);


    /**
     * 删除地址
     * @param userId
     * @param addressId
     * @return
     */
    Boolean deleteUserAddress(String userId, String addressId);

    /**
     * 修改默认地址
     * @param userId
     * @param addressId
     * @return
     */
    Boolean updateUserAddressToBeDefault(String userId, String addressId);
}
