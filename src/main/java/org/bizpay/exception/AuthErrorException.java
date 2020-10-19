package org.bizpay.exception;
// 디테일한 기능등은 추후에 만든다.
public class AuthErrorException extends RuntimeException{

	private static final long serialVersionUID = 8534753416569719789L;

	public AuthErrorException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuthErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AuthErrorException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AuthErrorException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AuthErrorException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
