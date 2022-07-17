package com.weixin.sell.service.impl;

import antlr.collections.Stack;
import com.weixin.sell.config.RedisLockConfig;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.service.SecKillService;
import com.weixin.sell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class SecKillServiceImpl implements SecKillService {

    private static final int TIMEOUT = 10 * 1000; //超时时间 10s

    @Autowired
    private RedisLockConfig redisLockConfig;

    /**
     * 国庆活动，皮蛋粥特价，限量100000份
     */
    static Map<String, Integer> products; // 餐品
    static Map<String, Integer> stock; // 库存
    static Map<String, String> orders; // 订单

    static {
        /**
         * 模拟多个表，商品信息表，库存表，秒杀成功订单表
         */
        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("12", 100000);
        stock.put("12", 100000);
    }

    private String queryMap(String productId) {
        return "国庆活动，皮蛋粥特价，限量份"
                + products.get(productId)
                + " 还剩：" + stock.get(productId) + " 份"
                + " 该商品成功下单用户数目："
                + orders.size() + " 人";
    }

    @Override
    public String querySecKillProductInfo(String productId) {
        return this.queryMap(productId);
    }

    @Override
    public void orderProductMockDiffUser(String productId) {
        // 加锁 当前时间+过期时间
        long time = System.currentTimeMillis() + TIMEOUT;
        // 加锁失败抛出异常
        if (!redisLockConfig.lock(productId, String.valueOf(time))) {
            throw new SellException(101, "当前页面过于拥挤,请刷新页面试试！");
        }
        // 1.查询该商品库存，为0则活动结束。
        int stockNum = stock.get(productId);
        if (stockNum == 0) {
            throw new SellException(100, "活动结束");
        } else {
            //2.下单(模拟不同用户openid不同)
            orders.put(KeyUtil.getUniqueKey(), productId);
            //3.减库存
            stockNum = stockNum - 1;
            try {
                Thread.sleep(100);  // 休眠100毫秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stock.put(productId, stockNum);
        }
        //解锁,该time加的锁由该time来解
        redisLockConfig.unLock(productId, String.valueOf(time));
    }
}
