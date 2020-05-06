package com.shop.user.client.controller.ex;

/**
 * 控制层异常，是所有控制层异常的基类
 */
public class ControllerException extends RuntimeException{

	private static final long serialVersionUID = 2890170796273067151L;

	public ControllerException() {
		super();
	}

	public ControllerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ControllerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ControllerException(String message) {
		super(message);
	}

	public ControllerException(Throwable cause) {
		super(cause);
	}
	
	
}
