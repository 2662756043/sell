package com.weixin.sell.service.impl;

import com.weixin.sell.dao.OrderDetailDao;
import com.weixin.sell.dto.CartDTO;
import com.weixin.sell.dto.OrderMasterDTO;
import com.weixin.sell.entity.OrderDetail;
import com.weixin.sell.enums.OrderStatusEnum;
import com.weixin.sell.enums.PayStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class OrderMasterServerImplTest {
    @Autowired
    private OrderMasterServerImpl orderMasterServer;

    private final String buyerId = "110110";
    private final String ORDER_ID = "1650786345948981225";

    @Test
    void create() throws Exception {
        OrderMasterDTO orderMasterDTO = new OrderMasterDTO();
        orderMasterDTO.setBuyerName("测试");
        orderMasterDTO.setBuyerAddress("地址");
        orderMasterDTO.setBuyerPhone("123456");
        orderMasterDTO.setBuyerOpenid(buyerId);


        // 购物车
        List<OrderDetail> orderDetailList = new ArrayList();
        // 创建订单详情对象
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProductId("123"); // id
        orderDetail.setProductQuantity(1);
        orderDetailList.add(orderDetail); // 将数据添加单最外层的对象中
        orderMasterDTO.setOrderDetailList(orderDetailList); // 存放list数据

        OrderMasterDTO orderMaster = orderMasterServer.create(orderMasterDTO);
        log.info("创建订单" + orderMaster);
        Assert.assertNotNull(orderMaster);
    }

    @Test
    public void findOne() throws Exception {
        OrderMasterDTO orderDTO = orderMasterServer.findOne("1650786345948981225");
        log.info("【订单详情】result={}" , orderDTO);
        Assert.assertNotNull(orderDTO);
    }

    @Test
    void findList() {
    }

    @Test
    void cancel() {
    }

    @Test
    void finished() {
        OrderMasterDTO orderDTO = orderMasterServer.findOne(ORDER_ID);
        OrderMasterDTO result = orderMasterServer.finished(orderDTO);
        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(), result.getOrderStatus());
    }

    @Test
    void paid() {
        OrderMasterDTO orderDTO = orderMasterServer.findOne(ORDER_ID);
        OrderMasterDTO result = orderMasterServer.paid(orderDTO);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(), result.getPayStatus());
    }
}
