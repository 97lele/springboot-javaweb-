package com.gdut.xg.shop.handler;

import com.gdut.xg.shop.enums.ErrorEnum;
import com.gdut.xg.shop.exceptions.InterceptorException;
import com.gdut.xg.shop.util.CommonUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lulu
 * @Date 2019/6/11 19:50
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getRequestURI().contains("addCart")){
    Cookie c= CommonUtil.getCookie(request, CommonUtil.USERCOOKIE);
    if(c!=null&&c.getValue()!=null) {
        return true;
    }else{
        throw new InterceptorException(ErrorEnum.NOTLOGIN);

    }
}
return true;
    }
}
