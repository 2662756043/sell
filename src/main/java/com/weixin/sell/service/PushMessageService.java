package com.weixin.sell.service;

import com.weixin.sell.dto.OrderMasterDTO;

public interface PushMessageService {
    /** 订单状态变更消息. */
    void orderStatus(OrderMasterDTO orderMasterDTO);

}
