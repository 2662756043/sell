package com.weixin.sell.exception;

import com.weixin.sell.VO.ResultVO;
import com.weixin.sell.config.ProjectUrlConfig;
import com.weixin.sell.utils.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SellerExceptionHandler {
    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    // 拦截登录异常，若发生异常，说明用户未登录，则引导用户重新登录
    @ExceptionHandler(SellerAuthorizeException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handlerAuthorizeException() {
        return "redirect:" +
                projectUrlConfig.getWechatOpenAuthorize() +
                "/sell/wechat/qrAuthorize" +
                "?returnUrl=" +
                projectUrlConfig.getSell() +
                "/sell/user/login";
    }

    @ExceptionHandler(SellException.class)
    @ResponseBody
    public ResultVO handlerSellException(SellException e) {
        ResultVO error = ResultVOUtils.error(e.getCode(), e.getMessage());
        return error;
    }
}