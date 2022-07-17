package com.weixin.sell.dao;


import com.weixin.sell.entity.ProductInfo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
class ProductInfoDaoTest {
    @Autowired
    private ProductInfoDao productInfoDao;

    @Test
    public void saveTest() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("123");
        productInfo.setProductName("测试");
        productInfo.setProductPrice(new BigDecimal(3.2));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("测试数据");
        productInfo.setProductIcon("http://");
        productInfo.setProductStatus(1);
        productInfo.setCategoryType(1);
        ProductInfo save = productInfoDao.save(productInfo);
        Assert.assertNotNull(save);

    }

    @Test
    void findByProductStatus() {
        List<ProductInfo> byProductStatus = productInfoDao.findByProductStatus(1);
        Assert.assertEquals(1, byProductStatus.size());
    }
}