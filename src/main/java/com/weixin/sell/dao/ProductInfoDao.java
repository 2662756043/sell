package com.weixin.sell.dao;


import com.weixin.sell.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductInfoDao extends JpaRepository<ProductInfo, String> {

    /**
     * 按照状态查找
     *
     * @param productStatus
     * @return
     */
    List<ProductInfo> findByProductStatus(Integer productStatus);
}
