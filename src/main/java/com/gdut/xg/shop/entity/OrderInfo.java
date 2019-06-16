package com.gdut.xg.shop.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lele
 * @since 2019-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInfo implements Serializable {

private static final long serialVersionUID=1L;

@TableId(type=IdType.ID_WORKER_STR)
    private String orderId;

    private String buyerName;

    private String createDate;

    private Float totalAmount;

    /**
     * 1完结，0处理中，-1撤销,-2已失效
     */
    private Integer status;


}
