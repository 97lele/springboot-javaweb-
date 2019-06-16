package com.gdut.xg.shop.exceptions;

import com.gdut.xg.shop.enums.ErrorEnum;
import lombok.Data;
import lombok.Getter;

/**
 * @author lulu
 * @Date 2019/6/15 11:01
 */
@Getter
public class OrderInfoException extends RuntimeException {
    private Integer code;
    public OrderInfoException (String message){
        super(message);
    }
    public OrderInfoException(ErrorEnum errorEnum){
        super(errorEnum.getMsg());
        this.code=code;
    }
    public OrderInfoException(String message,Integer code){
        super(message);
        this.code=code;
    }
}
