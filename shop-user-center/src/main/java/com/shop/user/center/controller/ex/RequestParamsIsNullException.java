package com.shop.user.center.controller.ex;

/**
 *请求参数为null
 */
public class RequestParamsIsNullException extends ControllerException{

	private static final long serialVersionUID = -1478950768560818554L;

	public RequestParamsIsNullException() {
		super();
	}

	public RequestParamsIsNullException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RequestParamsIsNullException(String message, Throwable cause) {
		super(message, cause);
	}

	public RequestParamsIsNullException(String message) {
		super(message);
	}

	public RequestParamsIsNullException(Throwable cause) {
		super(cause);
	}
	
}
