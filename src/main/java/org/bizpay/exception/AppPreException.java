package org.bizpay.exception;
// 앱통신 에러 메세지용 단순 에러 클래스 구분용이다
public class AppPreException extends Exception{

	private static final long serialVersionUID = -3475369765328422489L;
	final String CODE;

	public AppPreException(String message ,String codeMessage ) {
		super(message );
		CODE = codeMessage;
		// TODO Auto-generated constructor stub
	}
	public AppPreException(String message) {
		this(message , "");
		// TODO Auto-generated constructor stub
	}
	
	public String getCode() {
		return CODE;
	}
}
