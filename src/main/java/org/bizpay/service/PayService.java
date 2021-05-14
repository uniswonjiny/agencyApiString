package org.bizpay.service;

public interface PayService {
	// 영수증번호 생성
	public int rciptNo(String mberCode)throws Exception;
}
