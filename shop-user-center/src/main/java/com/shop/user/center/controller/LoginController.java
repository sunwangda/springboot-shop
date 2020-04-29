package com.shop.user.center.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shop.user.center.service.LoginService;
import com.shop.user.center.utils.JsonResult;
import com.shop.user.center.manual.model.LoginInfo;
import com.shop.user.center.manual.model.LoginInfo.LoginTypeEnum;
import com.shop.user.center.model.AdminLogin;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "login")
public class LoginController extends BaseController{
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping(value = "check", method = RequestMethod.POST)
	@ApiOperation(value = "检查登录", notes = "检测登录（admin，wx，qq）")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "loginType", value = "登录类型", required = false, type = "String"),
		@ApiImplicitParam(paramType = "query", name = "admin", value = "用户名", required = false, type = "String"),
		@ApiImplicitParam(paramType = "query", name = "password", value = "密码", required = false, type = "String")
	})
	public JsonResult<AdminLogin> Login(String loginType, String admin, String password) {
		AdminLogin data = null;
		if(LoginTypeEnum.ADMIN_LOGIN.equals(LoginTypeEnum.getLoginTypeEnum(loginType))) {
			data = loginService.loginService(admin, password);
		}
		return new JsonResult<>(SUCCESS, data);
	}
}
