package com.weixin.sell.service.impl;


import com.weixin.sell.convert.ConvertOrderMaster2OrderDTO;
import com.weixin.sell.dao.OrderDetailDao;
import com.weixin.sell.dao.OrderMasterDao;
import com.weixin.sell.dto.CartDTO;
import com.weixin.sell.dto.OrderMasterDTO;
import com.weixin.sell.entity.OrderDetail;
import com.weixin.sell.entity.OrderMaster;
import com.weixin.sell.entity.ProductInfo;
import com.weixin.sell.enums.OrderStatusEnum;
import com.weixin.sell.enums.PayStatusEnum;
import com.weixin.sell.enums.ResultEnum;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.service.OrderMasterServer;
import com.weixin.sell.service.PayService;
import com.weixin.sell.service.ProductInfoService;
import com.weixin.sell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单实现类
 */
@Service
@Slf4j
public class OrderMasterServerImpl implements OrderMasterServer {

    @Autowired
    private ProductInfoService productInfoService; // 商品信息
    @Autowired
    private OrderMasterDao orderMasterDao;
    @Autowired
    private OrderDetailDao orderDetailDao;

//    @Autowired
//    private PayService payService;

    @Override
    @Transactional // 创建订单
    public OrderMasterDTO create(OrderMasterDTO orderMasterDTO) {
        // 订单生成的id、
        String orderId = KeyUtil.getUniqueKey();

        //先定义一个总价
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        // 1、查询商品 数量 价格
        List<OrderDetail> orderDetailOpt = orderMasterDTO.getOrderDetailList(); // 拿到商品的list
        for (OrderDetail orderDetail : orderDetailOpt) {
            ProductInfo productInfo = productInfoService.findOne(orderDetail.getProductId()); // 根据id查
            //判断是否有商品，即数据库中是否有信息，若为空，说明商品不存在
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST); // 商品不存在
            }
            // 判断数量是否够
            // 2.计算订单总价
            //先计算某一件商品的总价乘以商品数量再加上原来的总价
            orderAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);

            // 3、写入两个数据库
            // 订单详情入库
            BeanUtils.copyProperties(productInfo, orderDetail);  // 第一个里面的信息赋值到第二个
            orderDetail.setDetailId(KeyUtil.getUniqueKey()); // 根基时间生成
            orderDetail.setOrderId(orderId); // 定义一个常量需要和订单的一样
            orderDetailDao.save(orderDetail); // 保存订单详情

            // 写入订单
            OrderMaster orderMaster = new OrderMaster();  // 创造订单实体类的对象
            orderMasterDTO.setOrderId(orderId); //  主键必须在上面 这个必须放在拷贝最上面 否则创建订单会报错
            BeanUtils.copyProperties(orderMasterDTO, orderMaster);  // 传入的信息赋值到orderMaster
            orderMaster.setOrderAmount(orderAmount);
            orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
            orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
            orderMasterDao.save(orderMaster); // 保存到订单

            // 4、扣库存    // 使用lombda循环数据存入 从前台传入的数据中获取几条订单进行循环(getOrderDetailList),
            // 将它转换成数据流的格式 放入map 创建容器来存放 getProductId和getProductQuantity最后转换成list
            List<CartDTO> cartDTOList = orderMasterDTO.getOrderDetailList().stream().map(e ->
                    new CartDTO(e.getProductId(), e.getProductQuantity())
            ).collect(Collectors.toList()); // 转换成list数据
            productInfoService.decreaseStock(cartDTOList);  // 保存
        }
        return orderMasterDTO;
    }

    /**
     * 查询单个
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderMasterDTO findOne(String orderId) {
        OrderMaster orderMaster = orderMasterDao.findById(orderId).get(); // 查到一条的数据
        if (orderMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailDao.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }
        OrderMasterDTO orderMasterDTO = new OrderMasterDTO();
        BeanUtils.copyProperties(orderMaster, orderMasterDTO);
        orderMasterDTO.setOrderDetailList(orderDetailList);
        return orderMasterDTO;
    }



    /**
     * 分页查询接
     *
     * @param buyerOpenid
     * @param pageable
     * @return
     */
    @Override
    public Page<OrderMasterDTO> findList(String buyerOpenid, Pageable pageable) {
        //1.查找用户订单
        Page<OrderMaster> orderMasterList = orderMasterDao.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderMasterDTO> convert = ConvertOrderMaster2OrderDTO.convert(orderMasterList.getContent());// 转换

        PageImpl<OrderMasterDTO> orderMasterDTOS = new PageImpl<>(convert, pageable, orderMasterList.getTotalElements());
        return orderMasterDTOS;
    }

    /**
     * 取消订单
     *
     * @param orderMasterDTO
     * @return
     */
    @Override
    @Transactional // 添加事务
    public OrderMasterDTO cancel(OrderMasterDTO orderMasterDTO) {
        // 创建订单对象
        OrderMaster orderMaster = new OrderMaster();
        // 获取状态
        Integer code = OrderStatusEnum.NEW.getCode();
        // 获取传入的订单状态
        Integer orderStatus = orderMasterDTO.getOrderStatus();
        // 1.判断订单状态，必须是新下的订单才能被取消（已完成和已取消状态下不能操作）
        if (!code.equals(orderStatus)) {
            log.error("【取消订单】 订单状态不正确：orderId={},orderStatus={}", orderMasterDTO.getOrderId(), orderMasterDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        // 2.修改订单状态，先改变DTO的状态，然后再拷贝属性，保证orderMaster和orderDTO的状态都改变了
        orderMasterDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderMasterDTO, orderMaster); // 全部拷贝到订单对象中
        OrderMaster result = orderMasterDao.save(orderMaster); // 保存
        if (result == null) {
            log.error("【取消订单】更新失败,orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_STATUS_UPDATE_FAIL);
        }
        // 3、增加库存
        if (CollectionUtils.isEmpty(orderMasterDTO.getOrderDetailList())) {
            log.error("【取消订单】订单中无商品详情,orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }
        List<CartDTO> cartDTOList = orderMasterDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productInfoService.increaseStock(cartDTOList);

        // 4.如果已经付款，需要退款
        if (PayStatusEnum.SUCCESS.getCode().equals(orderMasterDTO.getPayStatus())) {
            // TODO
//            payService.refund(orderMasterDTO);
        }
        return orderMasterDTO; // 返回处理过的信息

    }

    /**
     * 完成订单
     *
     * @param orderMasterDTO
     * @return
     */
    @Override
    @Transactional
    public OrderMasterDTO finished(OrderMasterDTO orderMasterDTO) {
        // 判断订单的状态    // 只有在新新单状态下才能被改成已完成
        if (!orderMasterDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【完结订单】订单状态不正确, orderId={}, orderStatus={}", orderMasterDTO.getOrderId(), orderMasterDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        // 修改订单状态 为完成
        orderMasterDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster(); // 创建对象
        BeanUtils.copyProperties(orderMasterDTO, orderMaster); // 传入的信息全部赋值
        OrderMaster updateResult = orderMasterDao.save(orderMaster);
        if (updateResult == null) {
            log.error("【完结订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_STATUS_UPDATE_FAIL);
        }

        return orderMasterDTO;
    }

    /**
     * 支付订单
     *
     * @param orderMasterDTO
     * @return
     */
    @Override
    @Transactional
    public OrderMasterDTO paid(OrderMasterDTO orderMasterDTO) {
        // 1、判断订单状态 如果订单状态是新订单
        if (!orderMasterDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【订单支付完成】订单状态不正确, orderId={}, orderStatus={}", orderMasterDTO.getOrderId(), orderMasterDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        // 2、判断支付状态
        if (!orderMasterDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【订单支付完成】订单支付状态不正确, orderDTO={}", orderMasterDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        // 3、修改支付状态
        orderMasterDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderMasterDTO, orderMaster);
        OrderMaster updateResult = orderMasterDao.save(orderMaster);
        if (updateResult == null) {
            log.error("【订单支付完成】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_STATUS_UPDATE_FAIL);
        }

        return orderMasterDTO;

    }

    // 分页查询所有并返回订单数据传输对象 卖家后端管理系统使用
    @Override
    public Page<OrderMasterDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterDao.findAll(pageable);
        List<OrderMasterDTO> orderDTOList = ConvertOrderMaster2OrderDTO.convert(orderMasterPage.getContent());
        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }
}
