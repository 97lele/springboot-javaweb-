package com.gdut.xg.shop.service.impl;

import java.io.UnsupportedEncodingException;

import java.util.ArrayList;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gdut.xg.shop.util.LRU.LRUList;
import com.gdut.xg.shop.util.LRU.Node;
import com.gdut.xg.shop.VO.CartVO;
import com.gdut.xg.shop.VO.ProductCartVO;
import com.gdut.xg.shop.VO.ProductVO;
import com.gdut.xg.shop.dao.UserDao;
import com.gdut.xg.shop.entity.User;
import com.gdut.xg.shop.service.ShopCartService;
import com.gdut.xg.shop.util.CommonUtil;
import com.gdut.xg.shop.util.HttpServletRequestUtil;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShopCartServiceImpl implements ShopCartService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserDao u;
    @Autowired
    private Gson gson;


    @Override
    public Cookie addCart(HttpServletRequest req) throws UnsupportedEncodingException {
        // TODO Auto-generated method stub
        String productId = HttpServletRequestUtil.getString(req, "id");
        Integer productCount = HttpServletRequestUtil.getInt(req, "count");
        Float productPrice = HttpServletRequestUtil.getFloat(req, "price");
        String productName = HttpServletRequestUtil.getString(req, "name");
        String imgurl = HttpServletRequestUtil.getString(req, "imgurl");

        //cookie不支持中文，只能转义

        Integer stock = HttpServletRequestUtil.getInt(req, "stock");
        Cookie cookie = CommonUtil.getCookie(req, CommonUtil.CARTCOOKIE);
        if (cookie != null) {

            String value=redisTemplate.opsForValue().get(cookie.getValue());
            CartVO cart = gson.fromJson(value, CartVO.class);
            List<ProductCartVO> list = gson.fromJson(cart.getList(), new TypeToken<List<ProductCartVO>>() {
            }.getType());
            Boolean isNew = true;
            Float totalPrice = 0f;
            for (ProductCartVO p : list) {
                if (productId.equals(p.getProductId())) {
                    p.setCount(productCount);
                    p.setPrice(productPrice);
                    p.setName(productName);
                    p.setStock(stock);
                    p.setImgurl(imgurl);
                    isNew = false;

                }
                totalPrice += p.getPrice() * p.getCount();
            }
            if (isNew) {
                ProductCartVO productCart = new ProductCartVO();
                productCart.setCount(productCount);
                productCart.setProductId(productId);
                productCart.setPrice(productPrice);
                productCart.setName(productName);
                productCart.setStock(stock);
                productCart.setImgurl(imgurl);
                list.add(productCart);
                totalPrice += productCart.getPrice() * productCart.getCount();
            }
            cart.setTotal(totalPrice);
            cart.setList(gson.toJson(list));
            String cartStr = gson.toJson(cart);
            String key = cookie.getValue();
            redisTemplate.opsForValue().set(key, cartStr);
            cookie = new Cookie(CommonUtil.CARTCOOKIE, key);
            cookie.setPath("/");
            cookie.setMaxAge(CommonUtil.MAXAGE * 5);


        } else {
            User u = this.u.selectOne(Wrappers.<User>lambdaQuery().eq(User::getToken, CommonUtil.getCookie(req, CommonUtil.USERCOOKIE).getValue()));
            CartVO cartVO = new CartVO();
            cartVO.setUserName(u.getAccount());
            ProductCartVO productCart = new ProductCartVO();
            productCart.setCount(productCount);
            productCart.setProductId(productId);
            productCart.setPrice(productPrice);
            productCart.setStock(stock);
            productCart.setName(productName);
            productCart.setImgurl(imgurl);
            List<ProductCartVO> list = new ArrayList<>();
            list.add(productCart);
            cartVO.setTotal(productPrice * productCount);
            cartVO.setList(gson.toJson(list));
            String cartStr = gson.toJson(cartVO);
            String key = CommonUtil.CARTCOOKIE + "-" + CommonUtil.GenerateKey();
            redisTemplate.opsForValue().set(key, cartStr);
            cookie = new Cookie(CommonUtil.CARTCOOKIE, key);
            cookie.setPath("/");
            cookie.setMaxAge(CommonUtil.MAXAGE * 5);


        }
        return cookie;
    }

    @Override
    public Cookie cleanCart(Cookie cookie) {
        // TODO Auto-generated method stub
        if (cookie == null) {
            return null;
        }
        redisTemplate.delete(cookie.getValue());
        cookie.setValue(null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        return cookie;
    }

    @Override
    public Cookie removeProduct(HttpServletRequest req) {
        // TODO Auto-generated method stub
        String productId = HttpServletRequestUtil.getString(req, "id");
        Cookie cookie = CommonUtil.getCookie(req, CommonUtil.CARTCOOKIE);
        String value=redisTemplate.opsForValue().get(cookie.getValue());
        CartVO cart = gson.fromJson(value, CartVO.class);
        List<ProductCartVO> list = gson.fromJson(cart.getList(), new TypeToken<List<ProductCartVO>>() {
        }.getType());

        Float totalPrice = cart.getTotal();
        for (int i = 0; i < list.size(); i++) {
            ProductCartVO p = list.get(i);
            if (productId.equals(p.getProductId())) {
                list.remove(p);
                totalPrice = totalPrice - (p.getCount() * p.getPrice());
                break;
            }
        }
        cart.setTotal(totalPrice);
        cart.setList(gson.toJson(list));
        String cartStr = gson.toJson(cart);
redisTemplate.opsForValue().set(cookie.getValue(),cartStr);

        return cookie;
    }

    @Override
    public CartVO getCart(Cookie cookie, LRUList<String, ProductVO> plist) {
        // TODO Auto-generated method stub
        String value=redisTemplate.opsForValue().get(cookie.getValue());
        List<ProductCartVO> list;
        CartVO cart = gson.fromJson(value, CartVO.class);

        if(cart.getList()==null){

            list = new ArrayList<ProductCartVO>();
        }else{
          list  = gson.fromJson(cart.getList(), new TypeToken<List<ProductCartVO>>() {
            }.getType());

        }

        if (plist != null && plist.size() > 0) {
            Set<ProductVO> total = new LinkedHashSet();
            Node<String, ProductVO> head = plist.getHead();
            while (head.getNext() != null) {
                head = head.getNext();
                total.addAll(head.getData());
            }

            cart.setProductList(gson.toJson(total));
        } else {
            cart.setProductList("");
        }

        //cookie解码

        cart.setList(gson.toJson(list));
        cart.setSuccess(true);
        return cart;

    }

}
