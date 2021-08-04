package org.bizpay.controller;

import java.util.ArrayList;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.net.URL;
import java.util.HashMap;

//import javax.net.ssl.HttpsURLConnection;

import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.common.domain.PaymentReqParam;
import org.bizpay.common.domain.external.OrderStatusInfo;
import org.bizpay.common.util.SmsUtil;
import org.bizpay.domain.ReturnMsg;
import org.bizpay.domain.link.LinkSms;
import org.bizpay.domain.link.SmsCardPayment;
import org.bizpay.domain.link.SmsInsert;
import org.bizpay.domain.link.SmsLink;
import org.bizpay.domain.link.SmsPayRequest;
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
	
	@ApiOperation(value="QR연동결제전고객취소" , notes = "QR연동결제 결제전 고객이 취소를 선택한 경우")
	@RequestMapping(value = "qrPayCancel", method = RequestMethod.POST)
	public ResponseEntity<String> qrPayCancel(
			@RequestBody PaymentReqParam param) throws Exception{
		log.info("qr코드 결제전 취소요청");
		System.out.println(param.toString());
		service.payPreCancel(param);
		
		return new ResponseEntity<>(HttpStatus.OK);
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
		@ApiImplicitParam(name="mberId" ,value = "유니코아판매자아이디", required=true , dataType="string"  ),
		@ApiImplicitParam(name="exorderNo" ,value = "상대방의 주문번호", required=true , dataType="string"  )
	})
	@RequestMapping(value = "orderInfo", method = RequestMethod.POST)
	public ResponseEntity<OrderStatusInfo> orderInfo(@RequestBody HashMap<String, Object> param) throws Exception{
		// 필수값 확인
		if(param.get("mberId") ==null ||  param.get("exorderNo")==null || "".equals(param.get("mberId"))  || "".equals(param.get("exorderNo"))) {
			return new ResponseEntity<>( HttpStatus.BAD_GATEWAY); 
		}
		
		return new ResponseEntity<>(service.exOrderInfo(param) , HttpStatus.OK); 
	}
	
	// sms 결제정보
	@ApiOperation(value="SMS 결제전정보확인" , notes = "SMS 링크로 발송된 결제정보를 확인한다.")
	@ApiImplicitParams({
		@ApiImplicitParam(name="slid" ,value = "sms 상품정보 키값", required=true , dataType="long"  )
	})
	@RequestMapping(value = "smspay/{id}", method = RequestMethod.GET)
	public ResponseEntity<SmsLink> orderInfo(@PathVariable("id") long id) throws Exception{
		
		return new ResponseEntity<>(service.selectSmsLinkInfo(id) , HttpStatus.OK); 
	}
	
	// sms 결제 진행 -- 결제 정보 보낸거 그대로 받아서 쓰자
	@RequestMapping(value = "smspay", method = RequestMethod.POST)
	public ResponseEntity<String> smspay(@RequestBody SmsPayRequest param) throws Exception{
		log.info(param.toString());
		service.payment(param);
		StringBuffer msgBuf = new StringBuffer();
		msgBuf.append( "[사업자, 개인 모두 사용 가능한");
		msgBuf.append("\\n");
		msgBuf.append( "간편결제 솔루션BizPay]");
		msgBuf.append("\\n");
		msgBuf.append( "결제 내역 :");
		msgBuf.append( smsUtil.getShortUrl( "https://admin.uni-core.co.kr/external/smsPayResult/"+  param.getId() ));
		msgBuf.append("\\n");
		msgBuf.append( "비즈페이 가입: https://shorturl.at/fvzGL" );
		smsUtil.sendShortSms(param.getMobilePhone(), "<비즈페이 SMS LINK 결제 내역>", msgBuf.toString(), param.getRecipient());
		return new ResponseEntity<>("ok" , HttpStatus.OK); 
	}
	
	// linksms 상품정보
	@ApiOperation(value="LINK결제전정보확인" , notes = "LINK결제 링크로 발송된 결제정보를 확인한다.")
	@ApiImplicitParams({
		@ApiImplicitParam(name="id" ,value = "유니코아판매자아이디", required=true , dataType="long"  )
	})
	@RequestMapping(value = "linkpay/{id}", method = RequestMethod.GET)
	public ResponseEntity<ArrayList<LinkSms>> linkpay(@PathVariable("id") long id) throws Exception{
		return new ResponseEntity<>(service.selectLinkSmsInfo(id) , HttpStatus.OK); 
	}
	
	@RequestMapping(value = "smsInsert", method = RequestMethod.POST)
	public ResponseEntity<String> smsInsert(@RequestBody SmsInsert param) throws Exception{
		service.insertSmsGoods(param);
		return new ResponseEntity<>( String.valueOf(param.getSmsLinkId()) , HttpStatus.OK); 
	}
	// sms 링크 카드 결제 정보 조히
	@RequestMapping(value = "smsCardInfo/{id}", method = RequestMethod.GET)
	public ResponseEntity<SmsCardPayment> smsCardInfo(@PathVariable("id") long id) throws Exception{
		return new ResponseEntity<>(service.selectSmsCardPayment(id) , HttpStatus.OK); 
	}
	
	//sms 링크 결제 완료시 왼료 정보 조회
	@RequestMapping(value = "smsPayResultInfo/{id}", method = RequestMethod.GET)
	public ResponseEntity<HashMap<String, Object>> smsPayResultInfo(@PathVariable("id") long id) throws Exception{
		return new ResponseEntity<>(service.smsPayResultInfo(id) , HttpStatus.OK); 
	}
	
	// sms 상품 정보 입력 - 링크입력에 관련 되서 입력만 하는것이다 나중에 없에버릴수 있음
//	@RequestMapping(value = "smsgoods", method = RequestMethod.POST)
//	public ResponseEntity<SmsLink> smsgoods(  @RequestBody SmsPayRequest param) throws Exception{
//		
//		return new ResponseEntity<>(service.selectSmsLinkInfo(id) , HttpStatus.OK); 
//	}
	
}
