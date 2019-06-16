package com.gdut.xg.shop.util;

import com.gdut.xg.shop.VO.ResultVO;
import com.gdut.xg.shop.enums.ErrorEnum;

import java.util.Map;

/**
 * @author lulu
 * @Date 2019/6/14 10:00
 */
public class ResultVOUtil {


    public static ResultVO success(Map<String, Object> map) {

        return new ResultVO().setSuccess(true).setData(map);

    }

    public static ResultVO success(Boolean b) {
        return new ResultVO().setSuccess(b);
    }

    public static ResultVO success(Map<String, Object> map, Boolean b) {

        return new ResultVO().setData(map).setSuccess(b);
    }

    public static ResultVO success(Map<String, Object> map, Boolean b,String msg) {

        return new ResultVO(map,b,msg,null);
    }

    public static ResultVO error(ErrorEnum errorEnum) {
        return new ResultVO(errorEnum);
    }
    public static ResultVO error(String msg,Integer code){
        return new ResultVO(null,false,msg,code);
    }


}
