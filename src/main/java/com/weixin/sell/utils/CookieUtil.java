package com.weixin.sell.utils;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CookieUtil {
    /**
     * 设置cookie
     */
    public static void set(HttpServletResponse response, String name, String value, Integer maxAge) {

        Cookie cookie = new Cookie(name, value); // 放name和value
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 获取cookie
     */
    public static Cookie get(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = readCookieMap(request);
        if (cookieMap.containsKey(name)) {
            return readCookieMap(request).get(name);
        }
        return null;
    }

    /**
     * 将cookie封装成map
     *
     * @param request
     * @return
     */

    public static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies).forEach(cookie -> cookieMap.put(cookie.getName(), cookie)); // 使用流的方式放入
        }
        return cookieMap;
    }

}
