package com.weixin.sell.config;


import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WechatMpConfig {
    @Autowired
    private WechatAccountConfig wechatAccountConfig; // 注入配置信息

    @Bean
    public WxMpService wxMpService() {
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage()); // 将设置的信息存储进去
        return wxMpService;
    }

    // 设置公众平台开发者账号和密匙
    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpInMemoryConfigStorage.setAppId(wechatAccountConfig.getMpAppId()); // 公众平台开发者账号的id
        wxMpInMemoryConfigStorage.setSecret(wechatAccountConfig.getMpAppSecret()); // 开发者账号对应的密匙
        return wxMpInMemoryConfigStorage;
    }
}
