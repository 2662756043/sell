package com.weixin.sell.convert;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weixin.sell.dto.OrderMasterDTO;
import com.weixin.sell.entity.OrderDetail;
import com.weixin.sell.enums.ResultEnum;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 表单的值转换成dto格式的
 */
@Slf4j
public class OrderForm2OrderDTO {
    // 将orderForm值传入 转换成OrderMasterDTO并返回
    public static OrderMasterDTO covert(OrderForm orderForm) {
        OrderMasterDTO orderMasterDTO = new OrderMasterDTO(); // 创建构造器
        orderMasterDTO.setBuyerName(orderForm.getName());
        orderMasterDTO.setBuyerPhone(orderForm.getPhone());
        orderMasterDTO.setBuyerAddress(orderForm.getAddress());
        orderMasterDTO.setBuyerOpenid(orderForm.getOpenid());

        Gson gson = new Gson();
        List<OrderDetail> orderDetailList = null;
        try {
            // 1参数是需要转的内容 2参数是转成什么类型的   转成list格式
            orderDetailList = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>() {}.getType());
        } catch (Exception e) {
            log.error("【对象装换】错误，string={}", orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR); // 订单参数错误
        }

        orderMasterDTO.setOrderDetailList(orderDetailList);
        return orderMasterDTO; // 返回dto类型
    }
}
