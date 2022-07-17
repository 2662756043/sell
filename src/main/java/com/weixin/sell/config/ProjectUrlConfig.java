package com.weixin.sell.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "project-url")
public class ProjectUrlConfig {
    // 微信公众号授权平台
    private String wechatMpAuthorize;
    /**
     * 微信公开放平台授权地址
     */
    private String wechatOpenAuthorize;
    /**
     * 点餐项目的路径
     */
    private String sell;
}
