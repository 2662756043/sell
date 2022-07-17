package com.weixin.sell.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 商品修改，新增表单
 */
@Data
public class ProductForm {

    private String productId; // 商品编号

    /**
     * 商品名字
     */
    @NotNull(message = "商品名字不能为空")
    private String productName;

    /**
     * 商品价格
     */
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格不能为负")
    private BigDecimal productPrice;

    @NotNull(message = "商品库存不能为空")
    @Min(value = 0, message = "商品库存不能为负数")
    private Integer productStock; // 库存

    @NotNull(message = "商品类目不能为空")
    private Integer categoryType; // 商品类别

    /**
     * 商品描述
     */
    private String productDescription;

    /**
     * 商品小图
     */
    private String productIcon;

}
