package com.gdut.xg.shop.exceptions;

import com.gdut.xg.shop.enums.ErrorEnum;
import lombok.Getter;

/**
 * @author lulu
 * @Date 2019/6/15 17:14
 */
@Getter
public class ProductException extends RuntimeException {
    private Integer code;
    public ProductException (String message){
        super(message);
    }
    public ProductException(ErrorEnum errorEnum){
        super(errorEnum.getMsg());
        this.code=code;
    }
    public ProductException(String message,Integer code){
        super(message);
        this.code=code;
    }
}
