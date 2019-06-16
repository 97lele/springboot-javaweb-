package com.gdut.xg.shop.VO;

import com.gdut.xg.shop.enums.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author lulu
 * @Date 2019/6/14 9:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ResultVO {

    private Map<String,Object> data;
    private Boolean success;
    private String msg;
    private Integer code;

    public ResultVO(ErrorEnum r){
        this.code=r.getCode();
        this.msg=r.getMsg();
        this.success=false;
    }


}
