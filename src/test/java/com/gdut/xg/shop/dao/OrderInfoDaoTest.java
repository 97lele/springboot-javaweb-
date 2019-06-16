package com.gdut.xg.shop.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdut.xg.shop.VO.OrderDataVO;
import com.gdut.xg.shop.VO.ProductDataVO;
import com.gdut.xg.shop.VO.ProductVO;
import com.gdut.xg.shop.entity.Product;
import com.gdut.xg.shop.service.IOrderInfoService;
import com.gdut.xg.shop.service.IProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author lulu
 * @Date 2019/6/14 22:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderInfoDaoTest {
    @Autowired
    private IOrderInfoService o;
    @Autowired
    private IProductService p;
    @Test
    public void getProductData() throws Exception {
        ProductDataVO p=o.getProductData(1);
        System.out.println("ol");
    }

    @Test
    public void getOrderData() throws Exception {
        OrderDataVO o=this.o.getOrderDataVO(30);
        System.out.println("ok");
    }

    @Test
    public void getProuduct(){
List<ProductVO> list=p.getRelatvieProduct("A",0,2);
//        System.out.println(list.get(0).getCategory().getName());
    }

}