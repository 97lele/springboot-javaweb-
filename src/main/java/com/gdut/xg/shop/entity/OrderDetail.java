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
public class OrderDetail implements Serializable {

private static final long serialVersionUID=1L;
    @TableId(type= IdType.ID_WORKER_STR)
    private String orderDetailId;

    private String orderId;

    private Integer productCount;

    private String productName;

    private Float productPrice;

    private String productId;

    private String productImg;


}
