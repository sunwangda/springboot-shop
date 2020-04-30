package com.shop.user.center.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.shop.user.center.dao.AdminLoginMapper;
import com.shop.user.center.model.AdminLogin;
import com.shop.user.center.model.AdminLoginExample;
import com.shop.user.center.service.LoginService;
import com.shop.user.center.service.ex.UserNotFoundException;

@Service
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	private AdminLoginMapper adminLoginMapper;
	
	@Override
	public AdminLogin loginService(String admin, String password) {
		List<AdminLogin> logins = new ArrayList<AdminLogin>();
		AdminLoginExample example = new AdminLoginExample();
		example.createCriteria().andAdminEqualTo(admin);
		logins = adminLoginMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(logins)) {
			throw new UserNotFoundException("用户数据不存在");
		}
		AdminLogin login = logins.get(0);
		
		return login;
	}
}
