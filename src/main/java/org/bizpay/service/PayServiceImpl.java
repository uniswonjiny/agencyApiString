package org.bizpay.service;

import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Log
@Service
public class PayServiceImpl implements PayService {

	@Override
	public int rciptNo(String mberCode) throws Exception {
		log.info("영수증번호조회");
		
		return 0;
	}

}
