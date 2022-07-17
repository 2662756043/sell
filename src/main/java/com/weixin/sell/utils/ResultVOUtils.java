package com.weixin.sell.utils;


import com.weixin.sell.VO.ResultVO;
import com.weixin.sell.enums.ResultEnum;

import java.net.StandardSocketOptions;

/**
 * 返回结果工具类
 */
public class ResultVOUtils {

    /**
     * 有返回值的
     */
    @SuppressWarnings("rawtypes") // 使用generics时忽略没有指定相应的类型
    public static ResultVO success(Object object) {
        ResultVO<Object> resultVO = new ResultVO<Object>();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    @SuppressWarnings("unchecked")
    public static ResultVO success() {
        return success(null);
    }

    /**
     * 错误时返回
     */
    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setMsg(msg);
        resultVO.setCode(code);
        return resultVO;
    }

}