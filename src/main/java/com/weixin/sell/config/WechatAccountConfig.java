package com.weixin.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {

    // 公众平台开发者账号的id
    private String mpAppId;
    // 开发者账号对应的密匙
    private String mpAppSecret;
    // 商户id
    private String mchId;
    // 商户密匙
    private String mchKey;
    // 商户证书地址
    private String KeyPath;
    // 微信支付异步通知
    private String notifyUrl;
    // 开发平台账号
    private String openAppId;
    // 开发平台账号对应的密钥
    private String openAppSecret;
    // 微信模板id
    private Map<String, String> templateId;
}
