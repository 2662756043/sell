package com.weixin.sell.service.impl;

import com.weixin.sell.entity.ProductCategory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Arrays;
import java.util.List;


/**
 * 测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class ProductCategoryServiceImplTest {
    @Autowired
    private ProductCategoryServiceImpl productCategoryService;

    //单元测试
    @Test
    void findOne() throws Exception {
        ProductCategory oneOpt = productCategoryService.findOne(1);
        Integer categoryId = oneOpt.getCategoryId();
        Assert.assertEquals(new Integer(1), categoryId); // 判断是否相同
    }

    @Test
    void findAll() throws Exception {
        List<ProductCategory> listOpt = productCategoryService.findAll();
        Assert.assertNotEquals(0, listOpt.size()); // 长度不等于0
    }

    @Test
    void findAllByCategoryTypeIn() throws Exception {
        List<ProductCategory> findOpt = productCategoryService.findAllByCategoryTypeIn(Arrays.asList(1, 2, 3, 4));
        Assert.assertNotEquals(0, findOpt.size());
    }

    @Test
   public void save() throws Exception {
        ProductCategory saveOpt = new ProductCategory("男生专项", 10);
        ProductCategory save = productCategoryService.save(saveOpt);
        Assert.assertNotNull(save);
    }
}