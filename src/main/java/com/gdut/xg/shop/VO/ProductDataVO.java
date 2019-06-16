package com.gdut.xg.shop.VO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDataVO {
private List<String> name;
private List<BigDecimal> count;
private List<Double> trade;


	
}
