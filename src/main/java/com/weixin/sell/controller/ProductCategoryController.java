package com.weixin.sell.controller;

import com.weixin.sell.VO.ProductInfoVO;
import com.weixin.sell.VO.ProductVO;
import com.weixin.sell.VO.ResultVO;
import com.weixin.sell.entity.ProductCategory;
import com.weixin.sell.entity.ProductInfo;
import com.weixin.sell.service.ProductCategoryService;
import com.weixin.sell.service.ProductInfoService;
import com.weixin.sell.utils.ResultVOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 买家端：买家商品
 */
@RestController
@RequestMapping("buyer/product")
public class ProductCategoryController {
    // 注入 商品信息服务层
    @Autowired
    private ProductInfoService productInfoService;
    //类目
    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/list")
    public ResultVO list() {

        // 1、 查询所有的上家商品
        List<ProductInfo> productInfoList = productInfoService.findUpAll();
        // 2、查询类目（一次性查询）
        List<Integer> categoryList = new ArrayList<>();
        // 类别需要重所有信息里面取
        for (ProductInfo productInfo : productInfoList) {
            Integer categoryType = productInfo.getCategoryType(); // 获取类型
            categoryList.add(categoryType); // 将类型放入类目list总
        }
        List<ProductCategory> categoryTypeInOpt = productCategoryService.findAllByCategoryTypeIn(categoryList);
        // 3、 数据拼装
        ArrayList<ProductVO> productVOList = new ArrayList<>();
        // 遍历类目
        for (ProductCategory productCategory : categoryTypeInOpt) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(productCategory.getCategoryName()); // 名称
            productVO.setCategoryType(productCategory.getCategoryType()); // 类型
            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            // 遍历商品详情
            for (ProductInfo productInfo : productInfoList) {
                // 判断type是否相同
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO); // 将第一个的值拷贝到第二个容器中
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfos(productInfoVOList); // 将list放入
            productVOList.add(productVO);
        }
        return ResultVOUtils.success(productVOList); // 返回数据
    }
}
