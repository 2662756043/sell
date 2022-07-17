package com.weixin.sell.controller;

import com.weixin.sell.dto.OrderMasterDTO;
import com.weixin.sell.enums.ResultEnum;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.service.OrderMasterServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/seller/order")
public class SellerOrderController {
    @Autowired
    private OrderMasterServer orderMasterServer;

    /**
     * 查询所有订单
     *
     * @param page 第几页
     * @param size 一页有几条
     * @return
     */
    @GetMapping("/list")
    public String list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                       Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<OrderMasterDTO> orderDTOPage = orderMasterServer.findList(pageRequest);
        model.addAttribute("orderDTOPage", orderDTOPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);

        return "order/list";
    }

    @GetMapping("/cancel")
    public String cancel(@RequestParam("orderId") String orderId,
                         Model model) {
        try {
            OrderMasterDTO orderDTO = orderMasterServer.findOne(orderId);
            orderMasterServer.cancel(orderDTO);
        } catch (SellException e) {
            log.info("【微信卖家取消订单】订单不存在，orderId={}", orderId);
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/sell/seller/order/list");
            return "common/error";
        }
        model.addAttribute("msg", ResultEnum.ORDER_CANCEL_SUCCESS.getMessage());
        model.addAttribute("url", "/sell/seller/order/list");
        return "common/success";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("orderId") String orderId,
                         Model model) {

        OrderMasterDTO orderDTO = null;
        try {
            orderDTO = orderMasterServer.findOne(orderId);
        } catch (SellException e) {
            log.info("【卖家端订单详情】，orderId={}", orderId);
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/sell/seller/order/list");
            return "common/error";
        }
        model.addAttribute("orderDTO", orderDTO);
        return "order/detail";
    }

    @GetMapping("/finish")
    public String finish(@RequestParam("orderId") String orderId,
                         Model model) {
        try {
            OrderMasterDTO orderDTO = orderMasterServer.findOne(orderId);
            orderMasterServer.finished(orderDTO);
        } catch (SellException e) {
            log.info("【微信卖家完结订单】订单不存在，orderId={}", orderId);
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/sell/seller/order/list");
            return "common/error";
        }
        model.addAttribute("msg", ResultEnum.ORDER_FINISH_SUCCESS.getMessage());
        model.addAttribute("url", "/sell/seller/order/list");
        return "common/success";
    }
}