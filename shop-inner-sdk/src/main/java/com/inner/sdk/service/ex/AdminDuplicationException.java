package com.inner.sdk.service.ex;

/**
 *账号已经存在 
 */
public class AdminDuplicationException extends ServiceException{

	private static final long serialVersionUID = -1298254894880849588L;

	public AdminDuplicationException() {
		super();
	}

	public AdminDuplicationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AdminDuplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdminDuplicationException(String message) {
		super(message);
	}

	public AdminDuplicationException(Throwable cause) {
		super(cause);
	}
	
}
