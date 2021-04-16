package org.bizpay.service;

import org.bizpay.common.domain.PaymentReqParam;

public interface PaymentService {
	// 결제요청
	public void paymentReq(PaymentReqParam param) throws Exception;
}
