package com.gdut.xg.shop.dao;

import com.gdut.xg.shop.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lele
 * @since 2019-06-11
 */
public interface OrderInfoDao extends BaseMapper<OrderInfo> {

    @Select("SELECT product_name as name,SUM(product_count) as count,SUM(product_price*product_count) as total FROM order_detail LEFT JOIN product ON product.`id`=order_detail.`product_id` WHERE product.`category` = #{categoryId} GROUP BY order_detail.product_name")
     List<Map<String,Object>> getProductData(@Param("categoryId")Integer categoryId);
    @Select("SELECT COUNT(*) as count,DATE(create_date) d,SUM(total_amount) as total FROM order_info GROUP BY DATE(create_date) HAVING d >=  DATE_SUB(CURRENT_DATE(),INTERVAL #{day} DAY) ORDER BY d ASC")
    List<Map<String,Object>> getOrderData(@Param("day")Integer day);


}
