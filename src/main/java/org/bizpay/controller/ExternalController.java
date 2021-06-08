package org.bizpay.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.common.domain.PaymentReqParam;
import org.bizpay.common.domain.external.OrderStatusInfo;
import org.bizpay.common.util.SmsUtil;
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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/ext")
@Api(tags = "외부연동결제")
@CrossOrigin(origins={"*"})
public class ExternalController {
	
	@Autowired
	SmsUtil smsUtil;
	
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
	public ResponseEntity<ExternalOrderInputParam> qrPayRequest(
			@RequestBody PaymentReqParam param) throws Exception{
		log.info("qr코드 결제요청");
		System.out.println(param.toString());
		ExternalOrderInputParam info = service.payRequest(param);
		
		return new ResponseEntity<>(info,HttpStatus.OK);
	}
	
	@ApiOperation(value="QR결제정보확인" , notes = "QR연동결제정보")
	@RequestMapping(value = "qrOrder/{orderNo}", method = RequestMethod.GET)
	public ResponseEntity<ExternalOrderInputParam> qrOrderInfo(@PathVariable("orderNo") long orderNo) throws Exception{
		log.info("qr코드용 외부 연동결제주문정보");
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
		rm.setType("2000");
		rm.setMessage("환불완료");
		service.payCancel(param);
		return new ResponseEntity<>(rm , HttpStatus.OK); // 에러 발생하면 서비스에서 핸들러 호출하도록 되어 있음 
	}
	
	@ApiOperation(value="QR결제노티" , notes = "QR결제노티")
	@RequestMapping(value = "notiSend", method = RequestMethod.POST)
	public ResponseEntity<ReturnMsg> notiSend(@RequestBody ExternalOrderInputParam param) throws Exception{
		log.info("결제완료나 결제전  노티 ");
		ReturnMsg rm = new ReturnMsg();
		rm.setType("100");
		rm.setMessage("노티호출완료");
		
		if(param.getNotiUrl()==null || param.getNotiUrl().trim().length() <10) {
			rm.setType("200");
			rm.setMessage("요청항목 누락");
			return new ResponseEntity<>(rm , HttpStatus.GATEWAY_TIMEOUT); 
		}
		boolean flag = true;
		
		// 유니코아 도 취소 처리해주어야 한다. 결제전 취소 요청일 경우 취소처리해야한다.
		//연속 리프레시나 이미 결제취소 요청한 정보가 들어 왔을때 유니코아 외부결제 정보만 취소안된 상태일 경우 심각한 문제를 야기한다
		if("9000".equals(  param.getStatus() )) {
			service.exOrderCancel(param);		
			// 내부 관리 코드문제로 다시설정
			param.setStatus("9000");
			
		}
		int count = 0;
		while (count<4) {
			flag = service.notiCallHttp(param);
			if(flag) {
				flag = true;
				break;
			}
			else count++;
		}
	
		if(!flag) {
			rm.setType("200");
			rm.setMessage("노티호출에러");
		}
		// 실패시 문자발송
		if(count>3) {
			// 사용자 정보 휴대폰 연락처 조회
			String moblileNumber = "";
			if(moblileNumber !=null && moblileNumber.length()>10) {
				smsUtil.sendShortSms(moblileNumber, "noti서버에러\n주문번호 : " + param.getExorderNo() + "\n주문명:"+param.getOrderName(), param.getMberId());				
			}
		}
		return new ResponseEntity<>(rm , HttpStatus.OK); 
	}
	
	// 결제정보 확인
	@ApiOperation(value="결제정보확인" , notes = "결제정보확인")
	@ApiImplicitParams({
		@ApiImplicitParam(name="memberId" ,value = "유니코아판매자아이디", required=true , dataType="string"  ),
		@ApiImplicitParam(name="orderNo" ,value = "상대방의 주문번호", required=true , dataType="string"  ),
		@ApiImplicitParam(name="orderName" ,value = "주문이름", required=false , dataType="string"  )
	})
	@RequestMapping(value = "orderInfo", method = RequestMethod.POST)
	public ResponseEntity<OrderStatusInfo> orderInfo(@RequestBody HashMap<String, Object> param) throws Exception{
		// 필수값 확인
		if(param.get("memberId") ==null ||  param.get("orderNo")==null || "".equals(param.get("memberId"))  || "".equals(param.get("orderNo"))) {
			return new ResponseEntity<>( HttpStatus.BAD_GATEWAY); 
		}
		
		return new ResponseEntity<>(service.exOrderInfo(param) , HttpStatus.OK); 
	}
}
