package com.weixin.sell.dao;

import com.weixin.sell.entity.OrderDetail;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailDaoTest extends TestCase {
    @Autowired
    private OrderDetailDao repository;
    private final String ORDERID = "11111111";

    @Test
    public void saveTest() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("12348");
        orderDetail.setOrderId(ORDERID);
        orderDetail.setProductIcon("http://xigua.jpg");
        orderDetail.setProductId("1212121");
        orderDetail.setProductName("西瓜粥");
        orderDetail.setProductPrice(new BigDecimal(3.2));
        orderDetail.setProductQuantity(2);
        OrderDetail result = repository.save(orderDetail);
        Assert.assertNotNull(result);
    }

    @Test
    public void testFindByOrderId() {
        PageRequest request = PageRequest.of(0, 3);
        List<OrderDetail> result = repository.findByOrderId(ORDERID);
        Assert.assertNotNull(String.valueOf(0), result.size());
    }
}
