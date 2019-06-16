package com.gdut.xg.shop.exceptions;

import com.gdut.xg.shop.enums.ErrorEnum;
import lombok.Getter;

/**
 * @author lulu
 * @Date 2019/6/15 18:13
 */
@Getter
public class InterceptorException extends RuntimeException {
    private Integer code;
    public InterceptorException (String message){
        super(message);
    }
    public InterceptorException(ErrorEnum errorEnum){
        super(errorEnum.getMsg());
        this.code=code;
    }
    public InterceptorException(String message,Integer code){
        super(message);
        this.code=code;
    }


}
