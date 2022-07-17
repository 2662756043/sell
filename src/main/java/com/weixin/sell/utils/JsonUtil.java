package com.weixin.sell.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JSON格式化工具
 */
public class JsonUtil {
    public static String toJson(Object object) {
        GsonBuilder json = new GsonBuilder();
        json.setPrettyPrinting();
        Gson gson = json.create(); // 创建json格式
        return gson.toJson(object); // 将对象转换成JSON格式并返回
    }
}
