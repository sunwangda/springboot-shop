package com.shop.user.client.controller;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.inner.sdk.enums.LoginInfo.LoginTypeEnum;
import com.inner.sdk.model.UserInfo;
import com.inner.sdk.request.UserInfoRequest;
import com.inner.sdk.service.UserService;
import com.inner.sdk.utils.JsonResult;
import com.shop.user.client.controller.ex.LoginTypeIsNotExistException;
import com.shop.user.client.controller.ex.RequestParamsIsNullException;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping(value = "user")
public class UserController extends BaseController{
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Reference
	private UserService userService;
	
	@Autowired
	private JedisPool jedisPool;
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ApiOperation(value = "检查登录", notes = "检测登录（admin，wx，qq）")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "loginType", value = "登录类型", required = false, type = "String"),
		@ApiImplicitParam(paramType = "query", name = "admin", value = "用户名", required = false, type = "String"),
		@ApiImplicitParam(paramType = "query", name = "password", value = "密码", required = false, type = "String")
	})
	public JsonResult Login(String loginType, String admin, String password, HttpSession session) {
		if(admin == null || password == null) {
			throw new RequestParamsIsNullException("请求参数为null");
		}
		if(LoginTypeEnum.getLoginTypeEnum(loginType) == null) {
			throw new LoginTypeIsNotExistException("登录方式不存在");
		}
		UserInfo data = null;
		if(LoginTypeEnum.ADMIN_LOGIN.equals(LoginTypeEnum.getLoginTypeEnum(loginType))) {
			data = userService.loginService(admin, password);
		}
		String token = UUID.randomUUID().toString();
		Jedis j = jedisPool.getResource();
		String key = data.getUserId()+"user";
		if(j.exists(key)) {
			j.del(key);
			System.out.println("删除存在的redis，key");
		}
		j.set(key,token);
		j.expire(key, 600);
		session.setAttribute("userId", data.getUserId());
		return new JsonResult(SUCCESS, data);
	}
	
	@RequestMapping(value = "register", method = RequestMethod.POST)
	@ApiOperation(value = "注册", notes = "注册")
	public JsonResult register(UserInfoRequest request) {
		userService.register(request);
		return new JsonResult(SUCCESS);
	}
	
	@RequestMapping(value = "test", method = RequestMethod.POST)
	@ApiOperation(value = "注册", notes = "注册")
	public JsonResult test() {
		System.out.println("sso测试正常");
		return new JsonResult(SUCCESS);
	}
	
}
