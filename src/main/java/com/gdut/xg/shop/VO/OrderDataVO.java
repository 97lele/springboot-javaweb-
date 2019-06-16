package com.gdut.xg.shop.VO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class OrderDataVO {
private List<Long> orderCount;
	private List<Double> amountList;
	private List<String> dateList;

	
}
