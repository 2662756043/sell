package com.weixin.sell.dao;

import com.weixin.sell.entity.OrderMaster;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterDaoTest extends TestCase {
    @Autowired
    private OrderMasterDao repository;
    private final String OPENID = "10010";
    @Test
    public void saveTest(){
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("123457");
        orderMaster.setBuyerName("张三的订单");
        orderMaster.setBuyerPhone("13754677890");
        orderMaster.setBuyerAddress("仙桃池");
        orderMaster.setBuyerOpenid(OPENID);
        orderMaster.setOrderAmount(new BigDecimal(8.8));
        OrderMaster result = repository.save(orderMaster);
        Assert.assertNotNull(result);
    }
    @Test
    public void testFindByBuyerOpenid() {
        PageRequest request = PageRequest.of(1,3);
        Page<OrderMaster> result = repository.findByBuyerOpenid(OPENID, request);
        System.out.println(result.getTotalElements());
        Assert.assertNotNull(String.valueOf(0),result.getTotalElements());
    }
}
