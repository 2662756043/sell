package com.weixin.sell.service.impl;

import com.weixin.sell.dao.SellerInfoDao;
import com.weixin.sell.entity.SellerInfo;
import com.weixin.sell.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerInfoDao sellerInfoDao;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        SellerInfo byOpenid = sellerInfoDao.findByOpenid(openid);
        return byOpenid;
    }
}
