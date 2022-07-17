package com.weixin.sell.dao;

import com.sun.xml.internal.ws.client.SEIPortInfo;
import com.weixin.sell.entity.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerInfoDao extends JpaRepository<SellerInfo, String> {
    SellerInfo findByOpenid(String openid);
}
