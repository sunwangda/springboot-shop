package com.inner.sdk.service;

import com.inner.sdk.model.UserInfo;
import com.inner.sdk.request.UserInfoRequest;

/**
 * 处理用户登录业务层接口
 */
public interface UserService {
	
	/**
	 * 
	 * @param admin 用户名
	 * @param password 密码
	 * @return 成功登录的用户数据
	 */
	UserInfo loginService(String admin, String password);
	
	void register(UserInfoRequest request);
	
}
