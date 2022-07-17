package com.weixin.sell.service;

import com.weixin.sell.entity.SellerInfo;

public interface SellerService {
    /** 通过openid查询卖家端信息 . */
    SellerInfo findSellerInfoByOpenid(String openid);
}
