package com.weixin.sell.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 用于表单验证 ：订单信息 为了进行数据绑定，属性名和表单中传过来的字段名保持一致
 */
@Data
public class OrderForm {
    /**
     * 买家姓名
     */
    @NotEmpty(message = "姓名必填")
    private String name;

    /**
     * 买家手机号
     */
    @NotEmpty(message = "手机号必填")
    private String phone;

    /**
     * 买家地址
     */
    @NotEmpty(message = "地址必填")
    private String address;

    /**
     * 买家微信 openid
     */
    @NotEmpty(message = "openid为必填")
    private String openid;


    /**
     * 购物车
     */
    @NotEmpty(message = "购物车不能为空")
    private String items;
}
