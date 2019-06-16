package com.gdut.xg.shop.VO;

import java.util.List;

import com.gdut.xg.shop.entity.OrderDetail;
import com.gdut.xg.shop.entity.OrderInfo;
import lombok.Data;

@Data
public class OrderModelVO {
	private String orderId;
	private String buyerName;
	private Float totalAmount;
	private String createDate;
	private Integer status;
	private List<OrderDetail> list;

	public OrderModelVO(OrderInfo e) {
		this.orderId=e.getOrderId();
		this.buyerName=e.getBuyerName();
		this.totalAmount=e.getTotalAmount();
		this.createDate=e.getCreateDate();
		this.status=e.getStatus();
		// TODO Auto-generated constructor stub
	}



}
