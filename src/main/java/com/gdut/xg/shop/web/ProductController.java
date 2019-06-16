package com.gdut.xg.shop.web;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.gdut.xg.shop.VO.ProductVO;
import com.gdut.xg.shop.VO.ResultVO;
import com.gdut.xg.shop.entity.Category;
import com.gdut.xg.shop.entity.Product;
import com.gdut.xg.shop.exceptions.ProductException;
import com.gdut.xg.shop.service.ICategoryService;
import com.gdut.xg.shop.service.IProductService;
import com.gdut.xg.shop.util.CommonUtil;
import com.gdut.xg.shop.util.HttpServletRequestUtil;
import com.gdut.xg.shop.util.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lele
 * @since 2019-06-11
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IProductService productService;

    @GetMapping("/getById")
    public ResultVO getById(@RequestParam("id") String id) {
        Product result = productService.getById(id);
        List<ProductVO> list = new ArrayList<>();

        for (Category c : categoryService.getCategory()) {
            if (result.getCategory().equals(c.getId())) {
                ProductVO p = new ProductVO(result, c);
                list.add(p);
                break;
            }
        }
        if (list.size() == 0) {
            ProductVO p = new ProductVO(result, null);
            list.add(p);
        }
        Map map = new HashMap<>();
        map.put("list", list);
        return ResultVOUtil.success(map);
    }

    @GetMapping("/getByList")
    public ResultVO getByList(HttpServletRequest req) {
        Integer pageSize = HttpServletRequestUtil.getInt(req, "pageSize");
        Integer pageIndex = HttpServletRequestUtil.getInt(req, "pageIndex");
        String name = HttpServletRequestUtil.getString(req, "name");
        Float highprice = HttpServletRequestUtil.getFloat(req, "highprice");
        Float lowprice = HttpServletRequestUtil.getFloat(req, "lowprice");
        Integer category = HttpServletRequestUtil.getInt(req, "category");
        Boolean b = Objects.nonNull(highprice) && Objects.nonNull(lowprice);
        IPage<Product> helper;
                LambdaQueryChainWrapper<Product> w= productService.<Product>lambdaQuery();
        if(Objects.nonNull(name)){
            w.like(Product::getName, name);
        }
        if(Objects.nonNull(category)){
            w.eq(Product::getCategory, category);
        }
        if(b){
            w .between(b, Product::getPrice, lowprice, highprice);
        }
             helper= w .page(new Page<>(pageIndex, pageSize));
        List<ProductVO> list = helper.getRecords().stream().map(e -> {
            ProductVO p = new ProductVO(e, null);
            for (Category c : categoryService.getCategory()) {

                if (e.getCategory().equals(c.getId())) {
                    p.setCategory(c);

                }
            }
            return p;
        }).collect(Collectors.toList());
        Map map = new HashMap();
        map.put("list", list);
        map.put("page", helper.getPages());
        return ResultVOUtil.success(map);
    }

    @GetMapping("/productRelative")
    public ResultVO productRelative(@RequestParam("id") String productId, @RequestParam("pageIndex") Integer pageIndex
            , @RequestParam("pageSize") Integer pageSize) {
        List<ProductVO> list = productService.getRelatvieProduct(productId, pageIndex, pageSize);
        Map map = new HashMap();
        map.put("list", list);
        return ResultVOUtil.success(map);
    }

    @PostMapping("/create")
    public ResultVO createProduct(HttpServletRequest req) {
        String name = HttpServletRequestUtil.getString(req, "name");
        Float price = HttpServletRequestUtil.getFloat(req, "price");
        Integer category = HttpServletRequestUtil.getInt(req, "category");
        Integer stock = HttpServletRequestUtil.getInt(req, "stock");
        //创建文件主图
        String imgUrl = null;
        try {
            Part part = req.getPart("file");
            if (part != null) {
                String fileName = CommonUtil.GenerateKey() + part.getSubmittedFileName();
                imgUrl = CommonUtil.generateFile(part.getInputStream(), fileName);
            }
        } catch (Exception e) {
            throw new ProductException(e.getMessage());
        }
        //描述
        String desc = HttpServletRequestUtil.getString(req, "desc");
        //是否折扣商品
        Integer dis = HttpServletRequestUtil.getInt(req, "dis");
        Integer isNew = HttpServletRequestUtil.getInt(req, "isnew");
        Product m = new Product(null, name, category, price, stock, isNew,
                dis, desc, imgUrl);
        return ResultVOUtil.success(this.productService.save(m));
    }

    @PostMapping("/delete")
    public ResultVO deleteProduct(@RequestParam("id")String id){
        Product p=productService.getById(id);
        CommonUtil.deleteFile(p.getProductImg());
        return ResultVOUtil.success(productService.removeById(id));
    }

    @PostMapping("/update")
    public ResultVO update(HttpServletRequest req){
        String name = HttpServletRequestUtil.getString(req, "name");
        Float price = HttpServletRequestUtil.getFloat(req, "price");
        Integer category = HttpServletRequestUtil.getInt(req, "category");
        String id = HttpServletRequestUtil.getString(req, "id");
        Integer stock = HttpServletRequestUtil.getInt(req, "stock");

        String imgUrl=null;
        try{
            Part part=req.getPart("file");

            if(part!=null&&part.getInputStream()!=null&&part.getInputStream().available()!=0) {
                if(part!=null) {
                    String fileName=CommonUtil.GenerateKey()+part.getSubmittedFileName();
                    imgUrl=CommonUtil.generateFile(part.getInputStream(), fileName);
                }
            }
        }catch (Exception e){
            throw new ProductException(e.getMessage());
        }
        //描述
        String desc=HttpServletRequestUtil.getString(req, "desc");
        //是否折扣商品
        Integer dis=HttpServletRequestUtil.getInt(req, "dis");
        Integer isNew=HttpServletRequestUtil.getInt(req, "isnew");
        Product m=new Product( id,  name,  category,  price,  stock, isNew,
                dis, desc, imgUrl);
        return ResultVOUtil.success(productService.updateById(m));
    }
}

