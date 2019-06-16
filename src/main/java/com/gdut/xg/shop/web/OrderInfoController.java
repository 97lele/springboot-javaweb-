package com.gdut.xg.shop.web;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdut.xg.shop.util.LRU.LRUList;
import com.gdut.xg.shop.util.LRU.Node;
import com.gdut.xg.shop.VO.*;
import com.gdut.xg.shop.entity.*;
import com.gdut.xg.shop.enums.ErrorEnum;
import com.gdut.xg.shop.exceptions.OrderInfoException;
import com.gdut.xg.shop.service.*;
import com.gdut.xg.shop.util.CommonUtil;
import com.gdut.xg.shop.util.HttpServletRequestUtil;
import com.gdut.xg.shop.util.ResultVOUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
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
@RequestMapping("/order")
public class OrderInfoController {
    @Autowired
    private IProductService p;
    @Autowired
    private IUserService u;
    @Autowired
    private IOrderInfoService o;
    @Autowired
    private ShopCartService s;
    @Autowired
    private SimpleDateFormat dateFormat;
    @Autowired
    private ICategoryService c;
    @Autowired
    private Gson gson;
    @Autowired
    private SimpleDateFormat format;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/getCart")
    public ResultVO getCart(HttpServletRequest req) {
        Cookie cookie = CommonUtil.getCookie(req, CommonUtil.CARTCOOKIE);
        if (cookie != null) {
            LRUList<String, ProductVO> list = (LRUList) req.getSession().getAttribute("historyList");

            Map map = new HashMap();
            map.put("cart", s.getCart(cookie, list));
            return ResultVOUtil.success(map);
        }
        return ResultVOUtil.error(ErrorEnum.CARTNOTINIT);
    }

    @GetMapping("/gethistory")
    public ResultVO getHistory(HttpServletRequest req) {
        Map<String, String> map = (HashMap) req.getSession().getAttribute("history");
        if (map != null && map.keySet() != null && map.keySet().size() > 0) {
            List<HistroyVO> hl = map.keySet().stream().map(e -> {
                String url = "http://localhost:8080/user/details.html?id=";
                String temp[] = e.split("-");
                url += temp[1] + "&name=" + temp[0] + "&discount=" + temp[5] + "&category=" + temp[7] + "&imgurl=" + temp[2] + "&isNew=" + temp[5] + "&description=" + temp[6] + "&price=" + temp[3] + "&stock=" + temp[8] + "&add=0";
                HistroyVO h = new HistroyVO();
                h.setId(temp[1]).setName(temp[0]).setImgurl(temp[2]).setPrice(temp[3]).setUrl(url);
                h.setLastViewDate(map.get(e));
                return h;
            }).collect(Collectors.toList());
            Map m = new HashMap();
            m.put("list", hl);
            return ResultVOUtil.success(m);

        }
        return ResultVOUtil.error(ErrorEnum.NOTHISTORY);

    }

    @GetMapping("/getorderlist")
    public ResultVO getOrderList(HttpServletRequest req) {
        Integer pageSize = HttpServletRequestUtil.getInt(req, "pageSize");
        Integer pageIndex = HttpServletRequestUtil.getInt(req, "pageIndex");
        String account = HttpServletRequestUtil.getString(req, "account");
        Cookie c = CommonUtil.getCookie(req, CommonUtil.USERCOOKIE);
        String token = null;
        if (c == null) {
            return ResultVOUtil.error(ErrorEnum.NOTLOGIN);
        }
        token = c.getValue();




        return ResultVOUtil.success(this.o.getOrderList(account, token, pageIndex, pageSize));
    }

    @GetMapping("/getById")
    public ResultVO getById(@RequestParam("id") String id) {
        OrderModelVO o = this.o.getOrder(id);
        List<OrderModelVO> list = Collections.singletonList(o);
        Map map = new HashMap();
        map.put("list", list);
        map.put("page", 1);
        return ResultVOUtil.success(map);
    }

    @GetMapping("/admin/getProductData")
    public ResultVO getProductData(HttpServletRequest req) {
        Cookie cookie = null;
        if ((cookie = CommonUtil.getCookie(req, CommonUtil.USERCOOKIE)) != null) {
            User u = this.u.<User>lambdaQuery().eq(User::getToken, cookie.getValue()).one();
            if (u != null && !CommonUtil.OFF.equals(u.getToken()) && u.getToken().equals(cookie.getValue())) {
                Integer categoryId = HttpServletRequestUtil.getInt(req, "categoryId");
                if(categoryId==null){
                    categoryId=1;
                }
                ProductDataVO d = this.o.getProductData(categoryId);
                Map map = new HashMap();
                map.put("dataList", d);
                return ResultVOUtil.success(map);


            }
        }
        return ResultVOUtil.error(ErrorEnum.NOTLOGIN);
    }


    @GetMapping("/admin/getOrderData")
    public ResultVO getOrderData(HttpServletRequest req) {
        Cookie cookie = null;
        if ((cookie = CommonUtil.getCookie(req, CommonUtil.USERCOOKIE)) != null) {
            User u = this.u.<User>lambdaQuery().eq(User::getToken, cookie.getValue()).one();
            if (u != null && !CommonUtil.OFF.equals(u.getToken()) && u.getToken().equals(cookie.getValue())) {
                Integer day = HttpServletRequestUtil.getInt(req, "day");
                OrderDataVO o = this.o.getOrderDataVO(day);
                Map map = new HashMap();
                map.put("dataList", o);
                return ResultVOUtil.success(map);
            }
        }
        return ResultVOUtil.error(ErrorEnum.NOTLOGIN);
    }

    @PostMapping("/addCart")
    public ResultVO addCart(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Cookie c=this.s.addCart(req);
            resp.addCookie(c);
        } catch (Exception e) {
            throw new OrderInfoException(e.getMessage());
        }
        return ResultVOUtil.success(true);
    }

    @PostMapping("/addHistory")
    public void addHistory(HttpServletRequest req, HttpServletResponse resp) {
        String productId = HttpServletRequestUtil.getString(req, "id");
        String productImg = HttpServletRequestUtil.getString(req, "imgurl");
        Float productPrice = HttpServletRequestUtil.getFloat(req, "price");
        String productName = HttpServletRequestUtil.getString(req, "name");
        Integer isNew = HttpServletRequestUtil.getInt(req, "isNew");
        Integer discount = HttpServletRequestUtil.getInt(req, "discount");
        String desc = HttpServletRequestUtil.getString(req, "desc");
        String category = HttpServletRequestUtil.getString(req, "category");
        Integer stock = HttpServletRequestUtil.getInt(req, "stock");
        Map<String, String> map = (HashMap) req.getSession().getAttribute("history");
        if (map != null) {
            map.put(productName + "-" + productId + "-" + productImg + "-" + productPrice + "-" + isNew + "-" + discount + "-" + desc + "-" + category + "-" + stock,
                    this.dateFormat.format(new Date(System.currentTimeMillis())));
            req.getSession().setAttribute("history", map);
        } else {
            map = new HashMap<>();
            map.put(productName + "-" + productId + "-" + productImg + "-" + productPrice + "-" + isNew + "-" + discount + "-" + desc + "-" + category + "-" + stock,

                    this.dateFormat.format(new Date(System.currentTimeMillis())));
            req.getSession().setAttribute("history", map);
        }
        LRUList<String, ProductVO> list = (LRUList) req.getSession().getAttribute("historyList");
        if (list != null) {
            Node<String, ProductVO> n = list.find(productId);
            if (n == null) {
                Product g = this.p.getById(productId);
                IPage<Product> il = this.p.<Product>lambdaQuery().eq(Product::getCategory, g.getCategory()).page(new Page<>(0, 2, false));
                List<Product> plist = il.getRecords();
                plist.add(0, g);
                List<ProductVO> l = plist.stream().map(e -> {
                    ProductVO p = new ProductVO(e, null);
                    for (Category c : c.getCategory()) {

                        if (e.getCategory().equals(c.getId())) {
                            p.setCategory(c);

                        }
                    }
                    return p;
                }).collect(Collectors.toList());
                n = new Node(productId, l);
            }
            list.insert(n);
        } else {
            list = new LRUList(3);
            Product g = this.p.getById(productId);
            IPage<Product> il = this.p.<Product>lambdaQuery().eq(Product::getCategory, g.getCategory()).page(new Page<>(0, 2, false));
            List<Product> plist = il.getRecords();
            plist.add(0, g);
            List<ProductVO> l = plist.stream().map(e -> {
                ProductVO p = new ProductVO(e, null);
                for (Category c : c.getCategory()) {
                    if (e.getCategory().equals(c.getId())) {
                        p.setCategory(c);
                    }
                }
                return p;
            }).collect(Collectors.toList());
            Node<String, ProductVO> n = new Node(productId, l);
            list.insert(n);
        }
        req.getSession().setAttribute("historyList", list);
    }

    @PostMapping("/cleancart")
    public ResultVO cleanCart(HttpServletRequest req, HttpServletResponse resp) {
        Cookie cookie = CommonUtil.getCookie(req, CommonUtil.CARTCOOKIE);
        resp.addCookie(this.s.cleanCart(cookie));
        return ResultVOUtil.success(true);
    }

    @PostMapping("/removeproduct")
    public ResultVO removeProduct(HttpServletRequest req, HttpServletResponse resp) {
        Cookie c = this.s.removeProduct(req);
        resp.addCookie(c);
        return ResultVOUtil.success(true);
    }

    @PostMapping("/balance")
    public ResultVO banlance(HttpServletRequest req, HttpServletResponse resp) {
        Cookie cookie = CommonUtil.getCookie(req, CommonUtil.CARTCOOKIE);
        String value=redisTemplate.opsForValue().get(cookie.getValue());
        CartVO cart = gson.fromJson(value, CartVO.class);
        List<ProductCartVO> list = gson.fromJson(cart.getList(), new TypeToken<List<ProductCartVO>>() {
        }.getType());

        OrderInfo o = new OrderInfo();
        o.setTotalAmount(cart.getTotal());
        o.setCreateDate(dateFormat.format(new Date(System.currentTimeMillis())));
        o.setStatus(0);
        List<OrderDetail> list2 = list.stream().map(e -> {
            OrderDetail m = new OrderDetail();
            m.setOrderId(o.getOrderId());
            m.setProductCount(e.getCount());
            m.setProductImg(e.getImgurl());
            try {
                m.setProductName(URLDecoder.decode(e.getName(), "utf-8"));
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            m.setProductId(e.getProductId());
            m.setProductPrice(e.getPrice());
            return m;
        }).collect(Collectors.toList());
        Boolean s = this.o.createOrder(CommonUtil.getCookie(req, CommonUtil.USERCOOKIE).getValue(), o, list2);
        if (s) {
            cart.setList(null);
            cart.setTotal(0f);
            list.clear();
            cart.setList(gson.toJson(list));
            redisTemplate.opsForValue().set(cookie.getValue(),gson.toJson(cart));
        }
        resp.addCookie(cookie);
        return ResultVOUtil.success(s);
    }

    @PostMapping("/changeStatus")
    public ResultVO changeStatus(HttpServletRequest req){
        Integer status=HttpServletRequestUtil.getInt(req, "status");
        String orderId=HttpServletRequestUtil.getString(req, "orderId");
        return ResultVOUtil.success(this.o.update(Wrappers.<OrderInfo>lambdaUpdate().set(OrderInfo::getStatus,status).ge(OrderInfo::getOrderId,orderId)));
    }

}
