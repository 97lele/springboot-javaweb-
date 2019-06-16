package com.gdut.xg.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdut.xg.shop.VO.OrderDataVO;
import com.gdut.xg.shop.VO.OrderModelVO;
import com.gdut.xg.shop.VO.ProductDataVO;
import com.gdut.xg.shop.dao.OrderDetailDao;
import com.gdut.xg.shop.dao.ProductDao;
import com.gdut.xg.shop.dao.UserDao;
import com.gdut.xg.shop.entity.OrderDetail;
import com.gdut.xg.shop.entity.OrderInfo;
import com.gdut.xg.shop.dao.OrderInfoDao;
import com.gdut.xg.shop.entity.Product;
import com.gdut.xg.shop.entity.User;
import com.gdut.xg.shop.enums.ErrorEnum;
import com.gdut.xg.shop.exceptions.OrderInfoException;
import com.gdut.xg.shop.service.IOrderDetailService;
import com.gdut.xg.shop.service.IOrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.xg.shop.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lele
 * @since 2019-06-11
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoDao, OrderInfo> implements IOrderInfoService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private IOrderDetailService orderDetailService;

    @Autowired
    private ProductDao p;

    @Override
    public Map<String,Object> getOrderList(String account, String token, Integer pageIndex, Integer pageSize) {
        String name = "";
        if (token != null) {
            User u = new User();
            u.setToken(token);
            u = userDao.selectOne(Wrappers.query(u));
            if (u.getRole() != 1) {
                name = u.getAccount();
            } else{
                if(account!=null){
                    name=account;
                }
            }

        }
        Page<OrderInfo> g=new Page<OrderInfo>(pageIndex, pageSize);
        IPage<OrderInfo> p = this.getBaseMapper().selectPage(g,Wrappers.<OrderInfo>lambdaQuery()
                .like(OrderInfo::getBuyerName, name));
        OrderDetail d = new OrderDetail();
        List<OrderModelVO> m = new ArrayList<>();
        for (OrderInfo e : p.getRecords()) {
            OrderModelVO s = new OrderModelVO(e);
            d.setOrderId(e.getOrderId());
            List<OrderDetail> list = orderDetailService.getBaseMapper().selectList(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, e.getOrderId()));
            s.setList(list);
            m.add(s);
        }
        Map map = new HashMap();
        map.put("list", m);
        map.put("page", p.getPages());
        return map;
    }

    @Override
    public OrderModelVO getOrder(String orderId) {
        OrderInfo m = this.getBaseMapper().selectById(orderId);
        List<OrderDetail> list = orderDetailService.getBaseMapper().selectList(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, orderId));
        OrderModelVO o = new OrderModelVO(m);
        o.setList(list);
        return o;
    }

    @Override
    public ProductDataVO getProductData(Integer categoryId) {
        ProductDataVO v = new ProductDataVO();
        List<String> name = new ArrayList();
        List<BigDecimal> count = new ArrayList();
        List<Double> trade = new ArrayList();
        this.getBaseMapper().getProductData(categoryId).stream().forEach(e -> {
            name.add(e.get("name").toString());
            count.add((BigDecimal) e.get("count"));
            trade.add((Double) e.get("total"));
        });
        v.setCount(count);
        v.setName(name);
        v.setTrade(trade);
        return v;
    }

    @Override
    public OrderDataVO getOrderDataVO(Integer day) {

        OrderDataVO o = new OrderDataVO();
        List<Long> orderCount = new ArrayList();
        List<Double> amountList = new ArrayList();
        List<String> dateList = new ArrayList();
        this.getBaseMapper().getOrderData(day).stream().forEach(e -> {
            orderCount.add((Long) e.get("count"));
            amountList.add((Double) e.get("total"));
            dateList.add(e.get("d").toString());
        });
        o.setAmountList(amountList);
        o.setDateList(dateList);
        o.setOrderCount(orderCount);
        return o;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createOrder(String token, OrderInfo orderModel, List<OrderDetail> list) {
        User user = userDao.selectOne(Wrappers.<User>lambdaQuery().eq(User::getToken, token));
        orderModel.setBuyerName(user.getAccount());
       Boolean b=this.save(orderModel);
        // TODO Auto-generated method stub
        //构建订单详情的查询语句
        for (OrderDetail o : list) {
            o.setOrderId(orderModel.getOrderId());
            o.setOrderDetailId(CommonUtil.GenerateKey());
            Product p = this.p.selectById(o.getProductId());
            p.setStock(p.getStock() - o.getProductCount());
            if (p.getStock() < o.getProductCount()) {
                throw new OrderInfoException(ErrorEnum.STOCKNOTENOUGH);
            }
            this.p.updateById(p);

        }
        orderDetailService.saveBatch(list);
        return b;
    }
}
