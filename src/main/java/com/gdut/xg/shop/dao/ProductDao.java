package com.gdut.xg.shop.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gdut.xg.shop.entity.Product;
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
public interface ProductDao extends BaseMapper<Product> {


    @Select("SELECT p.* FROM product p LEFT JOIN product_relation r ON p.`id`=r.`product_slave` ${ew.customSqlSegment}")
    List<Product> getRelativeProduct(@Param(Constants.WRAPPER)Wrapper wrapper, IPage<Product> page);
}
