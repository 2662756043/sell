package com.weixin.sell.exception;

import com.weixin.sell.enums.ResultEnum;
import lombok.Getter;
import org.omg.CORBA.SystemException;

/**
 * 自定义异常处理类
 */
@Getter
public class SellException extends RuntimeException {

    private Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public SellException(Integer code, String message) {
        super(message); // 继承父类的东西
        this.code = code;
    }

}
