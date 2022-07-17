package com.weixin.sell.dao;

import com.weixin.sell.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * dao是个接口 继承JpaRepository<对象实体层 ,主键类型></>
 */
public interface ProductCategoryDao extends JpaRepository<ProductCategory, Integer> {

    List<ProductCategory> findAllByCategoryTypeIn(List<Integer> categoryTypeList);
}
