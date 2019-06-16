package com.gdut.xg.shop.VO;

import lombok.Data;

@Data
public class ProductCartVO {
private String productId;
private Integer count;
private Float price;
private String name;
private Integer stock;
private String imgurl;


}
