package org.bizpay.exception;
// 디테일한 기능등은 추후에 만든다.
public class KeyErrorException extends RuntimeException{

	private static final long serialVersionUID = 3417552458403683917L;

	public KeyErrorException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KeyErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public KeyErrorException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public KeyErrorException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public KeyErrorException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
