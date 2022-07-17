package com.weixin.sell.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 卖家信息
 */
@Data
@Entity
public class SellerInfo {
    /**
     * 一定要和数据库的名字对应sellerId->sell_id
     */

    @Id
    private String sellerId;
    /**
     * 卖家用户名.
     */
    private String username;

    /**
     * 卖家密码.
     */
    private String password;

    /**
     * 卖家开放平台openid.
     */
    private String openid;
}
