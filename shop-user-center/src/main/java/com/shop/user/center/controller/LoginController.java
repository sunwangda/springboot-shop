package com.shop.user.center.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
	
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String swd() {
		return "swd";
	}
}
