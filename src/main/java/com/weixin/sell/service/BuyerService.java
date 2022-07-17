package com.weixin.sell.service;


import com.weixin.sell.dto.OrderMasterDTO;

public interface BuyerService {
    /**
     * 查询一个用户的某个订单
     */
    OrderMasterDTO findOrderOne(String openid, String orderId);

    // 取消订单
    OrderMasterDTO cancelOrder(String openid, String orderId);

}
