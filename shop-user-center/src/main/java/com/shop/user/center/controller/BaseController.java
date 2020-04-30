package com.shop.user.center.controller;

import java.util.Arrays;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ExceptionHandler;

import com.shop.user.center.controller.ex.ControllerException;
import com.shop.user.center.service.ex.ServiceException;
import com.shop.user.center.utils.JsonResult;


/**
 * 控制器类的基类
 */
public abstract class BaseController {
	
	/**
	 * 操作成功时的状态码
	 */
	public static final int SUCCESS = 2000;
	
	/**
	 * 从Session中获取当前登录的用户的id
	 * @param session HttpSession对象
	 * @return 当前登录的用户的id
	 */
	protected Integer getUidFromSession(HttpSession session) {
		return Integer.valueOf(session.getAttribute("uid").toString());
	}
	
	/**
	 * 从Session中获取当前登录的用户名
	 * @param session HttpSession对象
	 * @return 当前登录的用户名
	 */
	protected String getUsernameFromSession(HttpSession session) {
		return session.getAttribute("username").toString();
	}
	
	@ExceptionHandler({ServiceException.class,ControllerException.class})
	public JsonResult<Void> handleException(Throwable ex) {
		
		// 创建响应结果对象
		JsonResult<Void> jsonResult = new JsonResult<>(ex);
		
		// 获取异常名称
		String exName = ex.getClass().getSimpleName();
		
		// 逐一判断异常类型
		switch (exName) {
		case "UserNotFoundException":
			jsonResult.setState(4000);
			break;
		case "LoginTypeIsNotExistException":
			jsonResult.setState(3001);
			break;
		case "RequestParamsIsNullException":
			jsonResult.setState(3002);
			break;
		default:
			break;
		}
		
		return jsonResult;
	}
	
}