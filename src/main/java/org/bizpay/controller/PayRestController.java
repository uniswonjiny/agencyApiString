package org.bizpay.controller;
// 대외 연계 결제페이지

import org.bizpay.common.domain.PaymentReqParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/pay")
@CrossOrigin(origins={"*"})
@Api(tags = "대외연동결제 ")
public class PayRestController {
	
	@ApiOperation(value="대외연동결제" , notes = "대외연동결제처리")
	@RequestMapping(value = "externalPayment", method = RequestMethod.POST)
	public ResponseEntity<Void> externalPayment(PaymentReqParam param) throws Exception {
		
		return new ResponseEntity<>( HttpStatus.OK);
	}
}
