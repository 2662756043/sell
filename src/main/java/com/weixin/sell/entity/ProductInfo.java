package com.weixin.sell.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品
 */
@Entity
@Data
@DynamicUpdate
public class ProductInfo {

    @Id
    private String productId;

    /**
     * 名字.
     */
    private String productName;

    /**
     * 单价.
     */
    private BigDecimal productPrice;

    /**
     * 库存.
     */
    private Integer productStock;

    /**
     * 描述.
     */
    private String productDescription;

    /**
     * 小图.
     */
    private String productIcon;

    /**
     * 状态 0 正常 1已下架
     */
    private Integer productStatus;

    /**
     * 类目编号.
     */
    private Integer categoryType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // 时间格式
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // 时间格式
    private Date updateTime;


}
