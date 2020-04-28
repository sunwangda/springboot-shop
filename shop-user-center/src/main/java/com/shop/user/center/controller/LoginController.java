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


@RestController
public class LoginController {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LoginService loginService;
	
	@ResponseBody
	@RequestMapping(value = "/Adminlogin", method = RequestMethod.GET)
	public List<String> proof() {
		int result = loginService.checkAdminLogin(admin, password);
		
		return list;
	}
}
