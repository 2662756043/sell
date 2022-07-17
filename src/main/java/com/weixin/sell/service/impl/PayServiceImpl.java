package com.weixin.sell.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.JsonUtil;
import com.weixin.sell.dto.OrderMasterDTO;
import com.weixin.sell.enums.ResultEnum;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.service.OrderMasterServer;
import com.weixin.sell.service.PayService;
import com.weixin.sell.utils.MathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 支付
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    private static final String ORDER_NAME = "微信点餐订单";

    @Autowired
    private OrderMasterServer orderMasterServer;

    @Autowired
    private BestPayServiceImpl bestPayService;

    /**
     * 发起支付，返回预订单信息
     *
     * @param orderMasterDTO
     * @return
     */
    @Override
    public PayResponse create(OrderMasterDTO orderMasterDTO) {
        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderMasterDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderMasterDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderMasterDTO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信支付】发起支付, request={}", JsonUtil.toJson(payRequest));

        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("【微信支付】发起支付, response={}", JsonUtil.toJson(payResponse));
        return payResponse;
    }

    @Override
    public PayResponse notify(String notifyData) {
        // 1. 验证签名
        // 2. 检查支付状态
        // 3. 支付金额对比
        // 4. 支付人对比（下单人==支付人）
        // 1、2的检验best-pay-sdk已经帮我们做好了
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("【微信异步回调通知】response={}", JsonUtil.toJson(payResponse));
        // 查询订单
        OrderMasterDTO orderDTO = orderMasterServer.findOne(payResponse.getOrderId());
        if (orderDTO == null) {
            log.error("【微信支付通知】订单不存在；orderId={}", payResponse.getOrderId());
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        // 判断金额是否一致,一个是BigDecimal类型，一个是Double，由于可能存在精度的丢失，比如0.01变成0.0100000011001等
        // 所以可以假设如果两个金额相减小于0.01就认为金额相等
        Double orderAmount = orderDTO.getOrderAmount().doubleValue(); // 订单金额
        Double payAmount = payResponse.getOrderAmount(); // 订单支付的金额
        // 比较是否一样
        if (!MathUtil.equals(orderAmount, payAmount)) {
            log.error("【微信异步回调通知】orderId={}, 微信通知金额{}，系统金额{}", orderDTO.getOrderId(), payResponse.getOrderAmount(), orderDTO.getOrderAmount());
            throw new SellException(ResultEnum.WECHAT_PAY_NOTIFY_MONEY_VERIFY_ERROR);
        }
        // 相等修改支付状态
        orderMasterServer.paid(orderDTO); // 调用修改支付状态代码
        return payResponse; // 返回回调参数
    }

    /**
     * 退款
     *
     * @param orderMasterDTO
     * @return
     */
    @Override
    public RefundResponse refund(OrderMasterDTO orderMasterDTO) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderAmount(orderMasterDTO.getOrderAmount().doubleValue());
        refundRequest.setOrderId(orderMasterDTO.getOrderId());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信退款】request={}",JsonUtil.toJson(refundRequest));
        RefundResponse refundResponse = bestPayService.refund(refundRequest);
        log.info("【微信退款】response={}",JsonUtil.toJson(refundResponse));
        return refundResponse;
    }
}
