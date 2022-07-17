package com.weixin.sell.dto;

import lombok.Data;

/**
 * 购物车dta
 */

@Data
public class CartDTO {

    private String productId; // 商品id
    private Integer productQuantity; // 购买数量

    /**
     * 构造方法
     * @param productId
     * @param productQuantity
     */
    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
