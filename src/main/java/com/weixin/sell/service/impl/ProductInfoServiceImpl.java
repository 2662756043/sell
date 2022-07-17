package com.weixin.sell.service.impl;


import com.weixin.sell.dao.ProductInfoDao;
import com.weixin.sell.dto.CartDTO;
import com.weixin.sell.entity.ProductInfo;
import com.weixin.sell.enums.ProductStatusEnum;
import com.weixin.sell.enums.ResultEnum;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.service.ProductInfoService;
import com.weixin.sell.utils.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 服务实现类 商品  // 注入dao层
 */
@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoDao productInfoDao;

    @Override
    public ProductInfo findOne(String productId) {
        ProductInfo findInfoOpt = productInfoDao.findById(productId).get();
        return findInfoOpt;
    }

    @Override
    public List<ProductInfo> findUpAll() {
        // 查上架的方法
        List<ProductInfo> ProductStatusOpt = productInfoDao.findByProductStatus(ProductStatusEnum.up.getCode());
        return ProductStatusOpt;
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        Page<ProductInfo> findPage = productInfoDao.findAll(pageable);
        return findPage;
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        ProductInfo saveOpt = productInfoDao.save(productInfo);
        return saveOpt;
    }

    /**
     * 加库存
     *
     * @param cartDTOList
     */
    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            // 获取商品id
            String productId = cartDTO.getProductId();
            ProductInfo productInfo = productInfoDao.findById(productId).get();// 查数据库获取信息
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            // 增加库存 库存+购物车的数量
            Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
            productInfo.setProductStock(result); // 将加好的数量设置进去
            productInfoDao.save(productInfo); // 保存进数据库
        }
    }

    /**
     * 减库存
     * 是一个集合 要么全部成功要么全部失败 @Transactional
     *
     * @param cartDTOList
     */
    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            String productId = cartDTO.getProductId(); // 获取id
            // 查询
            ProductInfo productInfoOpt = productInfoDao.findById(productId).get();
            if (productInfoOpt == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            // 减库存 库存减去购物车的数量
            Integer number = productInfoOpt.getProductStock() - cartDTO.getProductQuantity();
            if (number < 0) {
                throw new SellException(ResultEnum.PRODUCT_STORK_ERROR); // 扣减失败
            }
            productInfoOpt.setProductStock(number); // 将扣减的库存设置回去
            productInfoDao.save(productInfoOpt); // 保存
        }

    }

    /**
     * 上架
     *
     * @param productId
     * @return
     */
    @Override
    public ProductInfo onSale(String productId) {
        Optional<ProductInfo> productInfoOpt = productInfoDao.findById(productId);
        if (!productInfoOpt.isPresent()) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        ProductInfo productInfo = productInfoOpt.get();
        productInfo.setProductStatus(ProductStatusEnum.up.getCode()); // 设置上架状态
        ProductInfo result = productInfoDao.save(productInfo);
        return result;
    }

    /**
     * 下架
     *
     * @param productId
     * @return
     */
    @Override
    public ProductInfo offSale(String productId) {
        Optional<ProductInfo> productInfoOpt = productInfoDao.findById(productId);
        if (!productInfoOpt.isPresent()) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        ProductInfo productInfo = productInfoOpt.get();
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        ProductInfo result = productInfoDao.save(productInfo);
        return result;
    }
}
