package com.weixin.sell.aspect;


import com.weixin.sell.constant.CookieConstant;
import com.weixin.sell.constant.RedisConstant;
import com.weixin.sell.exception.SellerAuthorizeException;
import com.weixin.sell.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * AOP校验用户登录::卖家登录授权切面
 */
@Aspect
@Component
@Slf4j
public class SellerAuthorizeAspect {

    @Autowired
    private StringRedisTemplate redisTemplate; // 注入redis

    // 除了SellerUserController（因为涉及登录登出），controller包下所有Seller开头的Controller中的所有方法都要经过验证
    @Pointcut("execution(public * com.weixin.sell.controller.Seller*.*(..)) && !execution(public * com.weixin.sell.controller.SellerUserController.*(..))")
    public void verify() {
    }

    //在执行verify之前切入
    @Before("verify()")
    public void doVerify() {
        // 1、为了拿到cookie，需要得到request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 2、从中拿到cookie
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);// 接口：可以直接通过类名.属性来调用常量和方法
        if (cookie == null) {
            log.warn("【登录校验】Cookie中查不到token");
            throw new SellerAuthorizeException();
        }
        // 3.如果存在就去redis中查找
        String tokenValue = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
        if (StringUtils.isEmpty(tokenValue)) {
            log.warn("【登录校验】Redis中查不到token");
            throw new SellerAuthorizeException();
        }
    }

}
