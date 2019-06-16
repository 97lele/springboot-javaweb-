package com.gdut.xg.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class User implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String account;

    private String password;

    private Integer role;

    private String token;

    private String address;

    private String mobile;

    private String email;

    private Integer status;
    public User(Integer userId, String account, String password, String address, String mobile, String email) {
        super();
        this.userId = userId;
        this.account = account;
        this.password = password;
        this.address = address;
        this.mobile = mobile;
        this.email = email;
    }


    public User(Integer userId, String account, String password, Integer role) {
        super();
        this.userId = userId;
        this.account = account;
        this.password = password;
        this.role = role;
    }

}
