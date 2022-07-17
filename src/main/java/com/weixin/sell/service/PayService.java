package com.weixin.sell.service;

import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;
import com.weixin.sell.dto.OrderMasterDTO;

/**
 * 支付接口
 */
public interface PayService {
    /**
     * 支付订单的创建
     *
     * @param orderMasterDTO
     * @return
     */
    PayResponse create(OrderMasterDTO orderMasterDTO);

    /**
     * 微信异步回调通知
     */
    PayResponse notify(String notifyData);

    /**
     * 退款
     */
    RefundResponse refund(OrderMasterDTO orderMasterDTO);
}
