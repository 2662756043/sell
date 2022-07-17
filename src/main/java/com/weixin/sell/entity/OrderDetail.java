package com.weixin.sell.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单详情表
 */
@Entity
@Data
@DynamicUpdate
public class OrderDetail {
    @Id // 主键加id注解
    /**主键*/
    private String detailId;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 商品编号
     */
    private String productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 单价
     */
    private BigDecimal productPrice;
    /**
     * 商品数量
     */
    private Integer productQuantity;
    /**
     * 商品图片
     */
    private String productIcon;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // 时间格式
    private Date createTime;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // 时间格式
    private Date updateTime;
}
