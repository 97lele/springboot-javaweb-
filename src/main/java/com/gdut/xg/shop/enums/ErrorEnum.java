package com.gdut.xg.shop.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author lulu
 * @Date 2019/6/14 13:58
 */
@Getter
@AllArgsConstructor
public enum ErrorEnum {
    USENOTEXISTS(-1001,"用户不存在"),PASSWORDERRO(-1002,"密码错误"),NOTLOGIN(-1003,"尚未登陆"),
    ROLEERROR(-1004,"权限错误"),USERFROZEN(-1005,"账号冻结"),CARTNOTINIT(-1006,"尚未添加商品"),
    NOTHISTORY(-1007,"暂无浏览历史"),INNERERROR(-1008,"内部错误"),STOCKNOTENOUGH(-1009,"库存不足")
    ;
    private Integer code;
    private String msg;

}
