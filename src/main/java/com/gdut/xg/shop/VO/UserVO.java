package com.gdut.xg.shop.VO;


import com.gdut.xg.shop.entity.User;
import lombok.Data;

@Data
public class UserVO {

	private Integer userId;
	
	private String account;
	
	private String password;
	
	private Integer role;
	private String address;
	private String mobile;
	private String email;
	private Integer status;
	


	public UserVO(User u) {
		this.userId = u.getUserId();
		this.account = u.getAccount();
		this.password = u.getPassword();
		this.role = u.getRole();
		this.address = u.getAddress()==null?"":u.getAddress();
		this.mobile = u.getMobile()==null?"":u.getMobile();
		this.email = u.getEmail()==null?"":u.getEmail();
		this.status = u.getStatus()==null?-1:u.getStatus();
	}
	
}
