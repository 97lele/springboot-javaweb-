package com.gdut.xg.shop.service;

import com.gdut.xg.shop.util.LRU.LRUList;
import com.gdut.xg.shop.VO.CartVO;
import com.gdut.xg.shop.VO.ProductVO;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


public interface ShopCartService {

	 Cookie addCart(HttpServletRequest req) throws UnsupportedEncodingException;
	 Cookie cleanCart(Cookie cookie) ;
	 Cookie removeProduct(HttpServletRequest req);
	public CartVO getCart(Cookie cookie, LRUList<String,ProductVO> list);
	
	
}
