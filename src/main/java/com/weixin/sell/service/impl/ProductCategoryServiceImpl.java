package com.weixin.sell.service.impl;

import com.weixin.sell.dao.ProductCategoryDao;
import com.weixin.sell.entity.ProductCategory;
import com.weixin.sell.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实现服务层中的所有方法 用implements
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;


    /**
     * 单条查询
     *
     * @param categoryId
     * @return
     */
    @Override
    public ProductCategory findOne(Integer categoryId) {
        // 移除了findOne方法使用这个查询
        ProductCategory finOneOpt = productCategoryDao.findById(categoryId).get();
        return finOneOpt;
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<ProductCategory> findAll() {
        List<ProductCategory> findAllOpt = productCategoryDao.findAll();
        return findAllOpt;
    }

    /**
     * 根据类型查所有
     *
     * @param categoryTypeList
     * @return
     */
    @Override
    public List<ProductCategory> findAllByCategoryTypeIn(List<Integer> categoryTypeList) {
        List<ProductCategory> CategoryTypeInOpt = productCategoryDao.findAllByCategoryTypeIn(categoryTypeList);
        return CategoryTypeInOpt;
    }

    /**
     * 更新或者保存
     *
     * @param productCategory
     * @return
     */
    @Override
    public ProductCategory save(ProductCategory productCategory) {
        ProductCategory saveOpt = productCategoryDao.save(productCategory);
        return saveOpt;
    }
}
