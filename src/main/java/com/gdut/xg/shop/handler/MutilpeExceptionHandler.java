package com.gdut.xg.shop.handler;

import com.gdut.xg.shop.VO.ResultVO;
import com.gdut.xg.shop.exceptions.InterceptorException;
import com.gdut.xg.shop.exceptions.OrderInfoException;
import com.gdut.xg.shop.exceptions.ProductException;
import com.gdut.xg.shop.util.ResultVOUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class MutilpeExceptionHandler {
    @ExceptionHandler(value=InterceptorException.class)
    public ResultVO handleInterceptorException(InterceptorException e){

      return ResultVOUtil.error(e.getMessage(),e.getCode());
    }
    @ExceptionHandler(value=OrderInfoException.class)
    public ResultVO handleOrderInfoException(OrderInfoException e){
        return ResultVOUtil.error(e.getMessage(),e.getCode());

    }

    @ExceptionHandler(value=ProductException.class)
    public ResultVO handleProductException(ProductException e){
        return ResultVOUtil.error(e.getMessage(),e.getCode());

    }
}