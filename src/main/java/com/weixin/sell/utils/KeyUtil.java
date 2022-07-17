package com.weixin.sell.utils;

import java.util.Random;

public class KeyUtil {
    /**
     * 生成唯一主键
     * 格式：时间+随机数
     * 为了防止多线程下产生相同的key，就不能保证key的唯一性，所以加上同步
     * @return
     */
    public static synchronized String getUniqueKey() {
        Random random = new Random();
        System.currentTimeMillis();
        // 100000~999999,六位随机数，不能使用random.nextInt(1000000)，这样不能保证一定是6位
        Integer number = random.nextInt(900000) + 100000;
        String randomNumber = System.currentTimeMillis() + String.valueOf(number);
        return randomNumber;
    }


//    public static void main(String[] args) {
//        getUniqueKey();
//        System.out.println(getUniqueKey());
//    }
}
