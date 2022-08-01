package com.wyf.service;

import com.wyf.popj.bo.SubmitOrderBO;

public interface OrderService {
    /**
     * 创建新订单
     * @param submitOrderBO
     */
    void createOrder(SubmitOrderBO submitOrderBO);
}
