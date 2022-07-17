package com.weixin.sell.convert;


import com.weixin.sell.dto.OrderMasterDTO;
import com.weixin.sell.entity.OrderMaster;
import org.springframework.beans.BeanUtils;




import java.util.List;
import java.util.stream.Collectors;


/**
 * 转换成dto
 */
public class ConvertOrderMaster2OrderDTO {

    public  static OrderMasterDTO  covert(OrderMaster orderMaster){
//        将实体类转换成dto
        OrderMasterDTO orderMasterDTO = new OrderMasterDTO();
        BeanUtils.copyProperties(orderMaster,orderMasterDTO);
        return orderMasterDTO;
    }

    public static List<OrderMasterDTO> convert(List<OrderMaster> orderMasterList) {
        return orderMasterList.stream().map(e -> covert(e))
                .collect(Collectors.toList());
    }
}
