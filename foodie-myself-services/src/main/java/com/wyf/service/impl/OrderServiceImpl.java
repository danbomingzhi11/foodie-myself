package com.wyf.service.impl;

import com.wyf.enums.OrderStatusEnum;
import com.wyf.mapper.*;
import com.wyf.popj.*;
import com.wyf.popj.bo.SubmitOrderBO;
import com.wyf.service.ItemsImgService;
import com.wyf.service.ItemsService;
import com.wyf.service.ItemsSpecService;
import com.wyf.service.OrderService;
import com.wyf.utils.RandomId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private ItemsSpecService itemsSpecService;

    @Autowired
    private ItemsImgService itemsImgService;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    // 创建新订单
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createOrder(SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        String leftMsg = submitOrderBO.getLeftMsg();
        int payMethod = submitOrderBO.getPayMethod();

        // 包邮费用设置为0
        Integer postAmount = 0;
        String orderId = RandomId.generatorId();

        // 通过addressId 查询信息添加到订单表
        UserAddress userAddress = userAddressMapper.selectByPrimaryKey(addressId);

        // 1. 新订单数据保存
        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setId(orderId);
        orders.setReceiverName(userAddress.getReceiver());
        orders.setReceiverMobile(userAddress.getMobile());
        orders.setReceiverAddress(userAddress.getProvince() + " "
                + userAddress.getCity() + " "
                + userAddress.getDistrict() + " "
                + userAddress.getDetail());
        orders.setIsComment(0);
        orders.setIsDelete(0);
        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());
        orders.setPostAmount(postAmount);
        orders.setPayMethod(payMethod);
        orders.setLeftMsg(leftMsg);

        // 创建itemSpecIds 对应的数组
        String itemSpecIdsList[] = itemSpecIds.split(",");
        Integer totalAmount = 0;    // 商品原价累计
        Integer realPayAmount = 0;  // 优惠后的实际支付价格累计

        // 2. 循环根据itemSpecIds保存订单商品信息表
        for (String specId : itemSpecIdsList) {
            // 根据specId查询 对应的规格表 调用对应的Service层
            ItemsSpec itemsSpec = itemsSpecService.selectSpecBySpecId(specId);
            Items items = itemsService.selectItemByItemId(itemsSpec.getItemId());
            ItemsImg itemImg = itemsImgService.selectImgMessageByItemId(itemsSpec.getItemId());

            // 后期从redis获取
            Integer buyCounts = 1;
            // 数量后期从购物车中获取
            totalAmount += itemsSpec.getPriceNormal() * 1;
            realPayAmount += itemsSpec.getPriceDiscount() * 1;

            OrderItems orderItems = new OrderItems();
            orderItems.setId(RandomId.generatorId());
            orderItems.setOrderId(orderId);
            orderItems.setItemId(itemsSpec.getItemId());
            orderItems.setItemName(items.getItemName());
            orderItems.setItemImg(itemImg.getUrl());
            orderItems.setBuyCounts(1);
            orderItems.setItemSpecId(itemSpecIds);
            orderItems.setItemSpecName(itemsSpec.getName());
            orderItems.setPrice(itemsSpec.getPriceDiscount());

            orderItemsMapper.insert(orderItems);

            // 在用户提交订单以后，规格表中需要扣除库存
            itemsSpecService.decreaseItemSpecStock(specId, buyCounts, itemsSpec.getStock());
        }

        // 1.1 创建订单
        orders.setTotalAmount(totalAmount);
        orders.setRealPayAmount(realPayAmount);
        ordersMapper.insert(orders);

        // 3. 保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);
    }
}
