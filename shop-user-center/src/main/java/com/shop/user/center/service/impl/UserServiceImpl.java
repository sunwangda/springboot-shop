package com.shop.user.center.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.inner.sdk.model.UserInfo;
import com.inner.sdk.request.UserInfoRequest;
import com.inner.sdk.service.UserService;
import com.inner.sdk.service.ex.AdminDuplicationException;
import com.inner.sdk.service.ex.InsertException;
import com.inner.sdk.service.ex.PasswordNotMatchException;
import com.inner.sdk.service.ex.UserNotFoundException;
import com.inner.sdk.utils.Md5;
import com.shop.user.center.dao.AdminLoginMapper;
import com.shop.user.center.model.AdminLogin;
import com.shop.user.center.model.AdminLoginExample;



@Service
@Component
public class UserServiceImpl implements UserService{
	
	@Autowired
	private AdminLoginMapper adminLoginMapper;
	
	@Override
	public UserInfo loginService(String admin, String password) {
		List<AdminLogin> logins = new ArrayList<AdminLogin>();
		AdminLoginExample example = new AdminLoginExample();
		example.createCriteria().andAdminEqualTo(admin.trim());
		logins = adminLoginMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(logins)) {
			throw new UserNotFoundException("用户数据不存在");
		}
		AdminLogin login = logins.get(0);
		if(!Md5.getMd5Password(password, login.getSalt()).equals(login.getPassword())) {
			throw new PasswordNotMatchException("密码不正确");
		}
		UserInfo info = new UserInfo();
		info.setAdmin(login.getAdmin());
		info.setUserId(login.getId());
		info.setUsername(login.getUserName());
		return info;
	}
	
	@Override
	public void register(UserInfoRequest request) {
		List<AdminLogin> logins = new ArrayList<AdminLogin>();
		AdminLoginExample example = new AdminLoginExample();
		example.createCriteria().andAdminEqualTo(request.getAdmin());
		logins = adminLoginMapper.selectByExample(example);
		if(!CollectionUtils.isEmpty(logins)) {
			throw new AdminDuplicationException("账号已经存在");
		}
		AdminLogin login = new AdminLogin();
		String salt = UUID.randomUUID().toString().toUpperCase();
		String password = Md5.getMd5Password(request.getPassword(), salt);
		login.setAdmin(request.getAdmin());
		login.setPassword(password);
		login.setUserName(request.getUsername());
		login.setSalt(salt);
		login.setCreateTime(LocalDateTime.now());
		login.setModifyTime(LocalDateTime.now());
		login.setIsDeleted((byte) 0);
		int effectNum = adminLoginMapper.insert(login);
		if(effectNum != 1) {
			throw new InsertException("添加用户数据出现未知错误，请联系系统管理员");
		}
	}
}
