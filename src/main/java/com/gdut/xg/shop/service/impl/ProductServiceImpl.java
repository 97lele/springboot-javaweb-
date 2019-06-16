package com.gdut.xg.shop.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdut.xg.shop.VO.ProductVO;
import com.gdut.xg.shop.entity.Category;
import com.gdut.xg.shop.entity.Product;
import com.gdut.xg.shop.dao.ProductDao;
import com.gdut.xg.shop.service.ICategoryService;
import com.gdut.xg.shop.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lele
 * @since 2019-06-11
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductDao, Product> implements IProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ICategoryService c;

    @Override
    public List<ProductVO> getRelatvieProduct(String productId, Integer pageIndex, Integer pageSize) {

       List<Product> pl= productDao.getRelativeProduct(Wrappers.query().eq("r.`product_master`", productId).orderByDesc("r.`product_relate`"), new Page<>(pageIndex, pageSize,false));
        List<ProductVO> list = pl.stream().map(e -> {
            Optional<Category> c = this.c.getCategory().stream().filter(l -> l.getId().equals(e.getCategory())).findFirst();
            System.out.println(c.isPresent());
            if (c.isPresent()) {
                return new ProductVO(e, c.get());
            }else{
                return null;
            }
        }).collect(Collectors.toList());
        return list;
    }
}
