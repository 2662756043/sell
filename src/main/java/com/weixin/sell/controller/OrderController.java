package com.weixin.sell.controller;


import com.weixin.sell.VO.ResultVO;
import com.weixin.sell.convert.OrderForm2OrderDTO;
import com.weixin.sell.dto.OrderMasterDTO;
import com.weixin.sell.enums.ResultEnum;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.form.OrderForm;
import com.weixin.sell.service.BuyerService;
import com.weixin.sell.service.OrderMasterServer;
import com.weixin.sell.utils.ResultVOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.PageRanges;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 买家:买家订单功能
 */
@RestController
@Slf4j
@RequestMapping("/buyer/order")
public class OrderController {
    @Autowired
    private OrderMasterServer orderMasterServer;
    @Autowired
    private BuyerService buyerService;


    @PostMapping("/create")
    // 1、创建订单 BindingResult绑定结果
    public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm, BindingResult bindingResult) {
        // 判断表单校验是否有错误
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确,orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        // 将表单的信息进行转换
        OrderMasterDTO covert = OrderForm2OrderDTO.covert(orderForm);
        // 判断下购物车是否为空
        if (CollectionUtils.isEmpty(covert.getOrderDetailList())) {
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.ORDER_CART_EMPTY);
        }
        OrderMasterDTO orderMasterResult = orderMasterServer.create(covert); // 调用创建订单的方法
        // 返回固定格式
        Map<String, String> map = new HashMap<>();
        map.put("orderId", orderMasterResult.getOrderId()); // 订单id
        return ResultVOUtils.success(map);
    }

    /**
     * 订单列表
     */
    @GetMapping("/list")
    public ResultVO<OrderMasterDTO> list(@RequestParam("openid") String openid,
                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (StringUtils.isEmpty(openid)) {
            log.error("【查询订单】错误，openid不能为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        // 查询到的订单中，订单详情不是必须的，orderDetailList却一致显示null，如果不想显示，可以不对空字段进行序列化
        // 这样返回到前端的数据就没有orderDetailList这个字段了
        Page<OrderMasterDTO> list = orderMasterServer.findList(openid, pageRequest); // 输入需要查的id分页查询
        return ResultVOUtils.success(list.getContent()); // 返回列表内容
    }


    // 订单详情
    @GetMapping("/detail")
    public ResultVO<OrderMasterDTO> detail(@RequestParam("openid") String openid, @RequestParam("orderId") String orderId) {

        OrderMasterDTO orderMasterDTO = buyerService.findOrderOne(openid, orderId);
        return ResultVOUtils.success(orderMasterDTO);
    }

    // 取消订单
    @PostMapping("/cancel")
    public ResultVO<OrderMasterDTO> cancel(@RequestParam("openid") String openid,
                                           @RequestParam("orderId") String orderId) {
        OrderMasterDTO orderDTO = buyerService.findOrderOne(openid, orderId);
        OrderMasterDTO cancel = orderMasterServer.cancel(orderDTO);  // 取消订单
        return ResultVOUtils.success(cancel);
    }

}
