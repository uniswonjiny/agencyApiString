package org.bizpay.service;

import org.bizpay.common.domain.PaymentReqParam;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Log
@Service
public class PaymentServiceImpl implements PaymentService {

	@Override
	public void paymentReq(PaymentReqParam param) throws Exception {
		log.info("결제요청");
		

	}

}
