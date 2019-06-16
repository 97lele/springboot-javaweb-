package com.gdut.xg.shop.task;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gdut.xg.shop.apriori.Apriori;
import com.gdut.xg.shop.apriori.ProductRecommond;
import com.gdut.xg.shop.dao.OrderDetailDao;
import com.gdut.xg.shop.dao.OrderInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AprioriTask {

    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private OrderInfoDao o;

    @Scheduled(cron = "* * 0/12 * * ?")//每隔12小时执行
    public void getData() throws SQLException {
        List<Map<String, Object>> commondList = orderDetailDao.getProductRecommondData(Wrappers.query().select("order_id as orderId", "product_id as productId").in("order_id", "SELECT order_id FROM order_info").orderByDesc("product_id"));
        List<ProductRecommond> rcommondList = commondList.stream().map(e -> {
            ProductRecommond productRecommond = new ProductRecommond();
            productRecommond.setOrderId(e.get("orderId").toString());
            productRecommond.setProductIds(e.get("productId").toString());
            return productRecommond;
        }).collect(Collectors.toList());
        Integer total = o.selectCount(null);
        List<Map<String, Object>> set = orderDetailDao.getProductRecommondData(Wrappers.query().select("count(1) as total", "product_id as productId").groupBy("product_id"));
        Map<String, Integer> init = new HashMap<String, Integer>();
        HashSet<String> h = new HashSet<String>();
        set.stream().forEach(l -> {
            if ((Integer) l.get("total") < Math.ceil(total * Apriori.support)) {
                h.add(l.get("productId").toString());
            }
            init.put(l.get("productId").toString(), (Integer) l.get("total"));
        });


        Map<String, String> map = new HashMap<>();
        for (ProductRecommond d : rcommondList) {
            if (map.containsKey(d.getOrderId())) {
                String value = map.get(d.getOrderId());
                map.put(d.getOrderId(), value + "," + d.getProductId());
            } else {
                map.put(d.getOrderId(), d.getProductId());
            }

        }
        Map<String, Integer> map2 = new HashMap();
        for (String orderId : map.keySet()) {
            if (map2.containsKey(map.get(orderId))) {
                map2.put(map.get(orderId), map2.get(map.get(orderId)) + 1);
            } else {
                map2.put(map.get(orderId), 1);
            }
        }

        Map<String, Double> strongRule = Apriori.getResult(map2, init, h);
        //先把所有数据清空
        orderDetailDao.deleteData();
        //进行插入操作
        for (String g : strongRule.keySet()) {
            String[] param = g.split(",");
            orderDetailDao.insertRelation(param[0], param[1], strongRule.get(g));
        }

    }

}
