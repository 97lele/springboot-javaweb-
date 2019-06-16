package com.gdut.xg.shop.service;

import com.gdut.xg.shop.VO.ProductVO;
import com.gdut.xg.shop.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lele
 * @since 2019-06-11
 */
public interface IProductService extends IService<Product> {
List<ProductVO> getRelatvieProduct(String productId,Integer pageIndex,Integer pageSize);
}
