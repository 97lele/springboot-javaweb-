package com.gdut.xg.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gdut.xg.shop.VO.OrderDataVO;
import com.gdut.xg.shop.VO.OrderModelVO;
import com.gdut.xg.shop.VO.ProductDataVO;
import com.gdut.xg.shop.entity.OrderDetail;
import com.gdut.xg.shop.entity.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lele
 * @since 2019-06-11
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    Map<String,Object> getOrderList(String account, String token, Integer pageIndex, Integer pageSize);
     OrderModelVO getOrder(String orderId);
     ProductDataVO getProductData(Integer categoryId);
    OrderDataVO getOrderDataVO(Integer day);
    Boolean createOrder(String token,OrderInfo orderModel,List<OrderDetail> list);

}
