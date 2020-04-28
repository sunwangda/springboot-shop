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

@Service
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	private AdminLoginMapper adminLoginMapper;
	
	@Override
	public Integer checkAdminLogin(String admin, String password) {
		List<AdminLogin> login = new ArrayList<AdminLogin>();
		AdminLoginExample example = new AdminLoginExample();
		example.createCriteria().andAdminEqualTo(admin).andPasswordEqualTo(password);
		login = adminLoginMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(login)) {
			return 0;
		}
		return 1;
	}
}
