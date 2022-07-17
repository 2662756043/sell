package com.weixin.sell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching  // 增加对缓存的支持
public class SellApplication {

    public static void main(String[] args) {

        SpringApplication.run(SellApplication.class, args);
    }

}
