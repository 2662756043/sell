package com.weixin.sell.service.impl;

import com.weixin.sell.config.WechatAccountConfig;
import com.weixin.sell.dto.OrderMasterDTO;
import com.weixin.sell.service.PushMessageService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class PushMessageServiceImpl implements PushMessageService {
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Override
    public void orderStatus(OrderMasterDTO orderMasterDTO) {
        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        // 设置微信模板消息id
        wxMpTemplateMessage.setTemplateId(wechatAccountConfig.getTemplateId().get("orderStatus"));
        // 设置openid
        wxMpTemplateMessage.setToUser(wechatAccountConfig.getOpenAppId());
        // first,keyword1...remark这些是模板定义的name
        List<WxMpTemplateData> data = Arrays.asList(
                new WxMpTemplateData("first", "亲，请记得收货。"),
                new WxMpTemplateData("keyword1", "微信点餐"),
                new WxMpTemplateData("keyword2", "18868812345"),
                new WxMpTemplateData("keyword3", orderMasterDTO.getOrderId()),
                new WxMpTemplateData("keyword4", orderMasterDTO.getOrderStatusEnum().getMessage()),
                new WxMpTemplateData("keyword5", "￥" + orderMasterDTO.getOrderAmount()),
                new WxMpTemplateData("remark", "欢迎再次光临！")
        );
        // 设置消息data
        wxMpTemplateMessage.setData(data);

        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
        } catch (WxErrorException e) {
            // 这里捕获了异常但是并没有作处理，因为消息是可有可无了，不能因为消息推送失败影响整个事务
            log.error("【微信模板消息】发送失败，{}", e);
        }
    }
}
