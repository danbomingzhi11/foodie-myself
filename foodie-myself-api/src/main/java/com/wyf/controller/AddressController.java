package com.wyf.controller;

import com.wyf.popj.UserAddress;
import com.wyf.popj.bo.AddressBO;
import com.wyf.service.AddressService;
import com.wyf.utils.JsonResult;
import com.wyf.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "地址相关接口", tags = {"地址相关接口"})
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "查询地址", notes = "查询地址", httpMethod = "GET")
    @RequestMapping("/list")
    public JsonResult selectAddressByUserId(@RequestParam String userId) {
        List<UserAddress> userAddressesList = addressService.selectAddressByUserId(userId);

        return JsonResult.ok(userAddressesList);
    }

    @ApiOperation(value = "新增地址", notes = "新增地址", httpMethod = "POST")
    @RequestMapping("/add")
    public JsonResult addAddress(@RequestBody AddressBO addressBO) {
        // 1.addressBO判断逻辑
        JsonResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }

        // 2.调用插入逻辑
        Boolean result = addressService.addAddress(addressBO);

        if (!result) {
            return JsonResult.errorMsg("数据插入失败");
        }
        return JsonResult.ok();
    }

    @ApiOperation(value = "用户修改地址", notes = "用户修改地址", httpMethod = "POST")
    @PostMapping("/update")
    public JsonResult update(@RequestBody AddressBO addressBO) {

        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return JsonResult.errorMsg("修改地址错误：addressId不能为空");
        }

        JsonResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }

        Boolean result = addressService.updateUserAddress(addressBO);

        if (!result) {
            return JsonResult.errorMsg("数据插入失败");
        }
        return JsonResult.ok();
    }

    @ApiOperation(value = "用户删除地址", notes = "用户删除地址", httpMethod = "POST")
    @RequestMapping("/delete")
    public JsonResult delete(
            @RequestParam String userId,
            @RequestParam String addressId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return JsonResult.errorMsg("");
        }

        Boolean result = addressService.deleteUserAddress(userId, addressId);

        if (!result) {
            return JsonResult.errorMsg("数据插入失败");
        }
        return JsonResult.ok();
    }

    @ApiOperation(value = "用户设置默认地址", notes = "用户设置默认地址", httpMethod = "POST")
    @RequestMapping("/setDefalut")
    public JsonResult setDefalut(
            @RequestParam String userId,
            @RequestParam String addressId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return JsonResult.errorMsg("");
        }

        Boolean result = addressService.updateUserAddressToBeDefault(userId, addressId);

        if (!result) {
            return JsonResult.errorMsg("数据插入失败");
        }
        return JsonResult.ok();
    }

    // 代码解耦判断逻辑
    private JsonResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return JsonResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return JsonResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return JsonResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return JsonResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return JsonResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return JsonResult.errorMsg("收货地址信息不能为空");
        }

        return JsonResult.ok();
    }
}
