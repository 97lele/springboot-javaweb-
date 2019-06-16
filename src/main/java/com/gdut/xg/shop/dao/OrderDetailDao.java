package com.gdut.xg.shop.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gdut.xg.shop.entity.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
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
public interface OrderDetailDao extends BaseMapper<OrderDetail> {


    @Select("SELECT ${ew.sqlSelect} FROM order_detail ${ew.customSqlSegment}")
     List<Map<String,Object>> getProductRecommondData(@Param(Constants.WRAPPER)Wrapper wrapper);
    @Delete("delete from product_relation")
    void deleteData();
    @Insert("insertRelation into product_relation values (#{p0},#{p1},#{p2})")
    int insertRelation(@Param("p0")String p0, @Param("p1")String p1, @Param("p2")Double p2);

}
