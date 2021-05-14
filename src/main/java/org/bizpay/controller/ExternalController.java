package org.bizpay.controller;

import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.common.domain.PaymentReqParam;
import org.bizpay.domain.ExternalOrderInfo;
import org.bizpay.service.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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
@Api(tags = "외부연동결제")
@CrossOrigin(origins={"*"})
public class ExternalController {
	
	@Autowired
	ExternalService service;
	
	@ApiOperation(value="QR연동결제정보 입력" , notes = "QR연동결제 사전 결제 정보입력")
	@RequestMapping(value = "qrOrderInput", method = RequestMethod.POST)
	public ResponseEntity<String> qrPay(
			@RequestBody ExternalOrderInputParam param) throws Exception{
		log.info("qr코드용 외부 연동결제주문정보 입력");

		if(service.insertExOrder(param) >0) {
			return new ResponseEntity<>( " http://dm1586000202893.fun25.co.kr:16903/external/qrpay/"+param.getSeq(), HttpStatus.OK);
		}else {
			return new ResponseEntity<>( "주문정보 생성에 문제가 발생했습니다.", HttpStatus.UNAUTHORIZED);
		}	
	}
	@ApiOperation(value="QR연동결제요청" , notes = "QR연동결제 요청")
	@RequestMapping(value = "qrPayRequest", method = RequestMethod.POST)
	public ResponseEntity<String> qrPayRequest(
			@RequestBody PaymentReqParam param) throws Exception{
		log.info("qr코드 결제요청");
		service.payRequest(param);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(value="QR결제정보확인" , notes = "QR연동결제정보")
	@RequestMapping(value = "qrOrder/{orderNo}", method = RequestMethod.GET)
	public ResponseEntity<ExternalOrderInfo> qrOrderInfo(@PathVariable("orderNo") long orderNo) throws Exception{
		log.info("qr코드용 외부 연동결제주문정보 입력");
		ExternalOrderInfo info = service.selectOrderInfo(orderNo);
		if(info !=null  ) {
			return new ResponseEntity<>( info, HttpStatus.OK);
		}else {
			return new ResponseEntity<>( null, HttpStatus.I_AM_A_TEAPOT);
		}	
	}
}
