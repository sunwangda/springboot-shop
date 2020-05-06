package com.shop.user.client.controller.ex;

/**
 * 登录类型不存在
 */
public class LoginTypeIsNotExistException extends ControllerException{

	private static final long serialVersionUID = 3719071379879734691L;

	public LoginTypeIsNotExistException() {
		super();
	}

	public LoginTypeIsNotExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LoginTypeIsNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginTypeIsNotExistException(String message) {
		super(message);
	}

	public LoginTypeIsNotExistException(Throwable cause) {
		super(cause);
	}
	
	
}
