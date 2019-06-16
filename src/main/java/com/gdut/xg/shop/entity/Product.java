package com.gdut.xg.shop.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Product implements Serializable {

private static final long serialVersionUID=1L;

@TableId(type= IdType.ID_WORKER_STR)
    private String id;

    private String name;

    private Integer category;

    private Float price;

    private Integer stock;

    private Integer isNew;

    private Integer discount;

    private String description;

    private String productImg;


}
