package org.bizpay.controller;

import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.common.domain.PaymentReqParam;
import org.bizpay.domain.ReturnMsg;
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
	// 내부 서버에서 처리하는 것이므로 코드처리 하지 않는다. 중계페이지에서 팝업 알람 화면 구성해야한다.
//	@ApiOperation(value="QR연동결제정보 입력" , notes = "QR연동결제 사전 결제 정보입력")
//	@RequestMapping(value = "qrOrderInput", method = RequestMethod.POST)
//	public ResponseEntity<String> qrPay(
//			@RequestBody ExternalOrderInputParam param) throws Exception{
//		log.info("qr코드용 외부 연동결제주문정보 입력");
//
//		if(service.insertExOrder(param) >0) {
//			return new ResponseEntity<>( param.getSeq() + "", HttpStatus.OK);
//		}else {
//			return new ResponseEntity<>( "주문정보 생성에 문제가 발생했습니다.", HttpStatus.UNAUTHORIZED);
//		}	
//	}
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
	public ResponseEntity<ExternalOrderInputParam> qrOrderInfo(@PathVariable("orderNo") long orderNo) throws Exception{
		log.info("qr코드용 외부 연동결제주문정보 입력");
		ExternalOrderInputParam info = service.selectOrderInfo(orderNo);
		if(info !=null  ) {
			return new ResponseEntity<>( info, HttpStatus.OK);
		}else {
			return new ResponseEntity<>( null, HttpStatus.BAD_GATEWAY);
		}	
	}
	
	@ApiOperation(value="QR결제취소" , notes = "QR결제취소하기")
	@RequestMapping(value = "exOrderCancel", method = RequestMethod.POST)
	public ResponseEntity<ReturnMsg> qrOrderCancel(@RequestBody ExternalOrderInputParam param) throws Exception{
		log.info("qr코드용 외부 연동결제주문정보 입력");
		ReturnMsg rm = new ReturnMsg();
		rm.setType("1000");
		rm.setMessage("승인완료");
		service.payCancel(param);
		return new ResponseEntity<>(rm , HttpStatus.OK); // 에러 발생하면 서비스에서 핸들러 호출하도록 되어 있음 
	
	}
}
