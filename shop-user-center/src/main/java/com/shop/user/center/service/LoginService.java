package com.shop.user.center.service;

import com.shop.user.center.model.AdminLogin;

/**
 * 处理用户登录业务层接口
 */
public interface LoginService {
	
	/**
	 * 
	 * @param admin 用户名
	 * @param password 密码
	 * @return 成功登录的用户数据
	 */
	AdminLogin loginService(String admin, String password);
	
}
