package com.wyf.controller;

import com.wyf.popj.bo.SubmitOrderBO;
import com.wyf.popj.vo.MerchantOrdersVO;
import com.wyf.popj.vo.OrderVO;
import com.wyf.service.OrderService;
import com.wyf.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RequestMapping("/orders")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;


    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public JsonResult create(
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response) {
        // 1.创建订单
        orderService.createOrder(submitOrderBO);

        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
        /**
         * 1001
         * 2002 -> 用户购买
         * 3003 -> 用户购买
         * 4004
         */
        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        //TODO: 对接支付接口 ，一定要自己写一个 或者搞一个
        return JsonResult.ok();
    }
}
