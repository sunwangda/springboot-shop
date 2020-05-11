package com.inner.sdk.model;

import java.io.Serializable;

/**
 * 用户相关信息
 */
public class UserInfo implements Serializable{

	private static final long serialVersionUID = 4752209420619598045L;
	
	private Integer userId;
	private String username;
	private String admin;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	
}
