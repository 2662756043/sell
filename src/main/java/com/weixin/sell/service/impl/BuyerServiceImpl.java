package com.weixin.sell.service.impl;

import com.weixin.sell.dto.OrderMasterDTO;
import com.weixin.sell.enums.ResultEnum;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.service.BuyerService;
import com.weixin.sell.service.OrderMasterServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 实现方法
 */
@Slf4j
@Service
public class BuyerServiceImpl implements BuyerService {
    @Autowired
    private OrderMasterServer orderMasterServer;

    @Override
    public OrderMasterDTO findOrderOne(String openid, String orderId) {
        OrderMasterDTO orderMasterOpt = orderMasterServer.findOne(orderId); // 根据订单id找订单
        String buyerOpenid = orderMasterOpt.getBuyerOpenid(); // 买家微信id
        if (!buyerOpenid.equals(openid)) {
            log.error("【查询订单】订单的openid不一致,openid={},orderId=", openid);
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }

        return orderMasterOpt; // 返回整个订单的信息
    }

    /**
     * 取消订单
     *
     * @param openid
     * @param orderId
     * @return
     */
    @Override
    public OrderMasterDTO cancelOrder(String openid, String orderId) {
        OrderMasterDTO orderMasterOpt = orderMasterServer.findOne(orderId); // 查订单
        if (orderMasterOpt == null) {
            log.error("【取消订单】查不到该订单, orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        String buyerOpenid = orderMasterOpt.getBuyerOpenid(); // 买家微信id
        if (!buyerOpenid.equals(openid)) {
            log.error("【查询订单】订单的openid不一致,openid={},orderId=", openid);
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderMasterServer.cancel(orderMasterOpt); // 调用取消方法
    }
}
