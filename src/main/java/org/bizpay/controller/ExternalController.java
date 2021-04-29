package org.bizpay.controller;

import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.service.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/ext")
@CrossOrigin(origins={"*"})
@Api(tags = "외부연동결제")
public class ExternalController {
	
	@Autowired
	ExternalService service;
	
	
	@ApiOperation(value="QR연동결제정보 입력" , notes = "QR연동결제 사전 결제 정보입력")
	@RequestMapping(value = "qrOrderInput", method = RequestMethod.POST)
	public ResponseEntity<String> qrPay(
			@RequestBody ExternalOrderInputParam param) throws Exception{
		log.info("qr코드용 외부 연동결제주문정보 입력");

		if(service.insertExOrder(param) >0) {
			return new ResponseEntity<>( "http://localhost:8081/external/qrpay/"+param.getSeq(), HttpStatus.OK);
		}else {
			return new ResponseEntity<>( "주문정보페이지 생성에 문제가 발생했습니다.", HttpStatus.I_AM_A_TEAPOT);
		}
		

	}
}
