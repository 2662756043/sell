package com.weixin.sell.controller;


import com.lly835.bestpay.model.PayResponse;
import com.weixin.sell.dto.OrderMasterDTO;
import com.weixin.sell.enums.ResultEnum;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.service.OrderMasterServer;
import com.weixin.sell.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 支付 返回界面
 */
@Controller
@RequestMapping("/pay")
public class PayController {
    @Autowired
    private OrderMasterServer orderMasterServer;

    @Autowired
    private PayService payService;

    @GetMapping("/create")
    public String create(@RequestParam("orderId") String orderId,
                         @RequestParam("returnUrl") String returnUrl,
                         Model model) {
        // 1、查询订单
        OrderMasterDTO orderOpt = orderMasterServer.findOne(orderId);
        if (orderOpt == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        // 2、发起支付
        PayResponse payResponse = payService.create(orderOpt);
        model.addAttribute("payResponse",payResponse);
        model.addAttribute("returnUrl", returnUrl);
        return "pay/create";
    }

    /**
     * 微信异步回调通知，以及支付成功后通知微信
     * @param notifyData 支付后微信携带的xml数据，故用@RequestBody
     * @return
     */
    @PostMapping("/notify")
    public String notify(@RequestBody String notifyData) {
        payService.notify(notifyData);
        return "pay/success";
    }
}
