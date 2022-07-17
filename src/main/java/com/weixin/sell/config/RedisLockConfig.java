package com.weixin.sell.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis 加锁
 */
@Component
@Slf4j
public class RedisLockConfig {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * redis加锁
     * value:当前时间+超时时间
     */
    public boolean lock(String key, String value) {
        // 如果key和value不存在就设置
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            log.info("redis加锁成功");
            return true;
        }
        /**
         *
         * redis键值已经存在的情况
         *
         * 乐观锁，类似CAS操作，这样使得没有获取锁的用户都不能成功操作，高并发下，可能由很多用户出现错误
         *
         * 不加这步会因为业务抛出异常造成死锁
         */
        // currentValue=A   这两个线程的value都是B ，结果是只有其中一个线程拿到锁
        String currentValue = redisTemplate.opsForValue().get(key); // 获取redis中的key值
        // if lock  Expired /** 判断当前新值是否小于当前系统的时间戳. */
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            // GETSET命令, 获取上一个锁的时间 /** 获取上一个锁【旧值】的时间，getAndSet类似i++ ,先获取，再设置. */
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
            /**
             * 如果键不存在，则返回null。如果键存在，得到的oldValue不是空，使用旧值与currentValue比较，不相同，则说明已经被其他线程修改过.
             */
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                /** 说明没有被其他线程修改，加锁成功. */
                return true;
            }
        }
        /** 已经超时，直接返回false,加锁失败. */
        return false;
    }

    /**
     * 解锁
     *
     * @param key
     * @param value
     */
    public void unLock(String key, String value) {
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            // currentValue.equals(value) 解锁还需上锁人
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                // 解锁其实就是删除key的操作
                redisTemplate.opsForValue().getOperations().delete(key);
                log.info("redis解锁成功");
            }
        } catch (Exception e) {
            log.error("【redis分布式锁】解锁异常, {}", e);
        }
    }
}
