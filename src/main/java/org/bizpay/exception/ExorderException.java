package org.bizpay.exception;

import org.bizpay.domain.OrderErrorType;

// 다른 에러와 구분하기 위해 별도로 만듬 
public class ExorderException extends RuntimeException{

	private static final long serialVersionUID = 8534753416569719789L;
	
	private OrderErrorType oet;

	public ExorderException() {
		super();
		// TODO Auto-generated constructor stub
	}
	// 결제시 에러 났을때 강제 취소 기능
	public ExorderException(OrderErrorType err) {
		super(err.getMessage());
		setOet(err);
		
	}
	
	public ExorderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ExorderException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ExorderException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ExorderException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	public OrderErrorType getOet() {
		return oet;
	}
	public void setOet(OrderErrorType oet) {
		this.oet = oet;
	}

}
