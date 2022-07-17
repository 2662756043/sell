package com.weixin.sell.config;

import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WechatPayConfig {
    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Bean
    public BestPayServiceImpl bestPayService() {
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayH5Config(wxPayH5Config());
        return bestPayService;
    }

    @Bean
    public WxPayH5Config wxPayH5Config() {
        WxPayH5Config wxPayH5Config = new WxPayH5Config();
        wxPayH5Config.setAppId(wechatAccountConfig.getMpAppId()); // 公众平台开发者账号的id
        wxPayH5Config.setAppSecret(wechatAccountConfig.getMpAppSecret());// 开发者账号对应的密匙
        wxPayH5Config.setKeyPath(wechatAccountConfig.getKeyPath());  // 商户证书地址
        wxPayH5Config.setMchId(wechatAccountConfig.getMchId()); // 商户id
        wxPayH5Config.setMchKey(wechatAccountConfig.getMchKey()); // 商户密匙
        wxPayH5Config.setNotifyUrl(wechatAccountConfig.getNotifyUrl()); // 微信支付异步通知
        return wxPayH5Config;
    }
}
