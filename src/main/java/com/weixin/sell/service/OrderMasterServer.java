package com.weixin.sell.service;

import com.weixin.sell.dto.OrderMasterDTO;
import com.weixin.sell.entity.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 订单server层
 */
public interface OrderMasterServer {
    /**
     * 创建订单
     */
    OrderMasterDTO create(OrderMasterDTO orderMasterDTO);

    /**
     * 查询单个订单
     */
    OrderMasterDTO findOne(String orderId);


    /**
     * 查询订单列表
     */
    Page<OrderMasterDTO> findList(String buyerOpenid, Pageable pageable);

    /**
     * 取消订单
     */
    OrderMasterDTO cancel(OrderMasterDTO orderMasterDTO);

    /**
     * 完结订单
     */
    OrderMasterDTO finished(OrderMasterDTO orderMasterDTO);

    /**
     * 订单支付
     */
    OrderMasterDTO paid(OrderMasterDTO orderMasterDTO);

    /**
     * 查询订单列表,卖家端管理后台使用.
     */
    Page<OrderMasterDTO> findList(Pageable pageable);


}
