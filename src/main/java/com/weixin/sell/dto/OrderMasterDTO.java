package com.weixin.sell.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.weixin.sell.entity.OrderDetail;
import com.weixin.sell.enums.CodeEnum;
import com.weixin.sell.enums.OrderStatusEnum;
import com.weixin.sell.enums.PayStatusEnum;
import com.weixin.sell.utils.EnumUtil;
import com.weixin.sell.utils.serializer.Date2LongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单数据传输对象：传输数据用的
 */
@Data
// 为空的字段不会被序列化，可以在application.yml中做全局配置
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderMasterDTO {
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 买家名字
     */
    private String buyerName;
    /**
     * 买家手机号
     */
    private String buyerPhone;
    /**
     * 买家地址
     */
    private String buyerAddress;
    /**
     * 买家微信Openid
     */
    private String buyerOpenid;
    /**
     * 订单总金额
     */
    private BigDecimal orderAmount;
    /**
     * 订单状态 默认为0新下单
     */
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();
    /**
     * 支付状态 默认为0未支付
     */
    private Integer payStatus = PayStatusEnum.WAIT.getCode();
    /**
     * 创建时间,自动转化Date类型.
     */
//    @JsonSerialize(using = Date2LongSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新时间.
     */
//    @JsonSerialize(using = Date2LongSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // 时间格式
    private Date updateTime;

    List<OrderDetail> orderDetailList;  // 多条订单

    // @JsonIgnore会忽略对该字段的序列化
    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum() {
        return EnumUtil.getByCode(orderStatus, OrderStatusEnum.class);
    }

    @JsonIgnore
    public PayStatusEnum getPayStatusEnum() {
        return EnumUtil.getByCode(payStatus, PayStatusEnum.class);
    }

}
