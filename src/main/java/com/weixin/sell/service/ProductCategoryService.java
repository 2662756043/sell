package com.weixin.sell.service;

import com.weixin.sell.entity.ProductCategory;

import java.util.List;

/**
 * 类目接口  提供方法
 */
public interface ProductCategoryService {
    /**
     * 根据id单条查询
     */
    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll(); // 查询所有

    /**
     * 通过type查所有
     */
    List<ProductCategory> findAllByCategoryTypeIn(List<Integer> categoryTypeList);

    /**
     * 新增和更新
     */
    ProductCategory save(ProductCategory productCategory);


}
