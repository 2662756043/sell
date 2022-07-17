package com.weixin.sell.VO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 返回给前端的商品信息 商品详情（出于安全考虑，不直接使用ProductInfo）
 */
@Data
public class ProductInfoVO implements Serializable{

    private static final long serialVersionUID = -6463033438238617190L;
    @JsonProperty("id")
    private String productId;

    @JsonProperty("name")
    private String productName;

    @JsonProperty("price")
    private BigDecimal productPrice;

    @JsonProperty("description")
    private String productDescription;

    @JsonProperty("icon")
    private String productIcon;
}