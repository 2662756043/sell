package com.weixin.sell.dao;

import com.weixin.sell.entity.OrderDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 订单详情dao
 */
public interface OrderDetailDao extends JpaRepository<OrderDetail, String> {
    /**
     * 根据订单id获取订单详情
     *
     * @return
     */
    List<OrderDetail> findByOrderId(String orderId);
}

