package com.gdut.xg.shop.util;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestUtil {

	public static Integer getInt(HttpServletRequest request,String name) {
		try {
			return Integer.decode(request.getParameter(name));

		}catch(Exception e) {
			return null;
		}
	}
	public static Float getFloat(HttpServletRequest request,String name) {
		try {
			return Float.valueOf(request.getParameter(name));

		}catch(Exception e) {
			return null;
		}
	}
	public static String  getString(HttpServletRequest request,String name) {
		try {
			String s=request.getParameter(name);
			if(s.trim().length()<=0) {
				return null;
			}else {
				return s;
			}

		}catch(Exception e) {
			return null;
		}
	}
}
