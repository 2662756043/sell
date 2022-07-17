package com.weixin.sell.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 类目 实体层
 */
@Entity
@DynamicUpdate  // 动态更新时间
@Data
public class ProductCategory {

    /**
     * 类目id(使用自增主键).
     */
    @Id
    // @GeneratedValue:主键的产生策略，通过strategy属性指定。
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 不添加这个会报一个表不存在
    private Integer categoryId;

    /**
     * 类目名字.
     */
    private String categoryName;

    /**
     * 类目编号.
     */
    private Integer categoryType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // 时间格式
    private Date createTime;

    /**
     * 更新时间(mysql5.7以上支持自动更新).
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // 时间格式
    private Date updateTime;

    public ProductCategory() {
    }

    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }
}
