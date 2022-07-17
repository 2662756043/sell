package com.weixin.sell.utils;

import java.math.BigDecimal;

public class MathUtil {

    public static final Double MONEY_RANGE = 0.01;

    /**
     * 比较金额是否相同
     */
    public static Boolean equals(Double d1, Double d2) {
        Double result = Math.abs(d1 - d2); // 相减
        if (result < MONEY_RANGE) {
            return true;
        } else {
            return false;
        }
    }

}
