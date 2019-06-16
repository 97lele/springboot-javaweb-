package com.gdut.xg.shop.handler;

import com.gdut.xg.shop.entity.User;
import com.gdut.xg.shop.enums.ErrorEnum;
import com.gdut.xg.shop.exceptions.InterceptorException;
import com.gdut.xg.shop.service.IUserService;
import com.gdut.xg.shop.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lulu
 * @Date 2019/6/15 18:18
 */

public class AdminInterceptor implements HandlerInterceptor {
    @Autowired
    private IUserService u;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().contains("admin")) {
            Cookie cookie = null;
            if ((cookie = CommonUtil.getCookie(request, CommonUtil.USERCOOKIE)) != null) {
                User u = this.u.<User>lambdaQuery().eq(User::getToken, cookie.getValue()).one();
                if (u != null && !CommonUtil.OFF.equals(u.getToken()) && u.getToken().equals(cookie.getValue())) {
                    if (u.getRole() != 1) {
                        throw new InterceptorException(ErrorEnum.ROLEERROR);
                    }

                } else {
                    throw new InterceptorException(ErrorEnum.NOTLOGIN);
                }
            }

        }
        return true;
    }
}