package org.bizpay.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;

import org.bizpay.common.util.KSPayMsgBean;
import org.bizpay.domain.ReturnMsg;
import org.bizpay.exception.AppPreException;
import org.bizpay.exception.AuthErrorException;
import org.bizpay.exception.ExorderException;
import org.bizpay.exception.KeyErrorException;
import org.bizpay.exception.SqlErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;

@ControllerAdvice
@Api(tags = "에러처리 ")
public class APIExceptionHandler {
	
	@Autowired
	KSPayMsgBean ksBean;
	
	// exception.class 처리
	@ExceptionHandler({DataAccessException.class ,Exception.class })
	public  ResponseEntity<String> sqlNormal(SQLException e) {
		return new ResponseEntity<String> ("서버등에 문제가 있습니다. 잠시후 이용해주세요" , HttpStatus.UNAUTHORIZED);  
	 }
	
	// 외부 연동 결제 관련 에러 처리용 핸들러
	// ExorderException
	@ExceptionHandler({ExorderException.class})
	public ResponseEntity<ReturnMsg> ExorderExceptionHandler(ExorderException e){
		// 결제진행후 에러 였던 경우결제 취소 진행
		try {
			//여기서도 에러가 발생하면 방법이 없다.
			if(e.getOet().getPTransactionNo()!=null) {
				Hashtable xht = ksBean.sendCardCancelMsg(
						e.getOet().getKSNET_PG_IP(), 				// -필수- ipaddr  X(15)   *KSNET_IP(개발:210.181.28.116, 운영:210.181.28.137)  
					    e.getOet().getKSNET_PG_PORT(),			// -필수- port   9( 5)   *KSNET_PORT(21001)  
						e.getOet().getPStoreId(), 								// -필수- pStoreId  X(10)   *상점아이디(개발:2999199999, 운영:?)  
						e.getOet().getPKeyInType(),						// -필수- pKeyInType   X(12)  KEY-IN유형(K:직접입력,S:리더기사용입력)  
						e.getOet().getPTransactionNo()						// -필수- pTransactionNo  X( 1)  *거래번호(승인응답시의 KEY:1로시작되는 12자리숫자)  
					);
			}
			
				
		} catch (Exception e2) {
			e2.fillInStackTrace();
		}
		
		
		ReturnMsg dto = new ReturnMsg();
		String  type= e.getMessage();
		dto.setType(type);
		if( "C001".equals(type) ) {
			dto.setType("2011");
			dto.setMessage("이미 취소 처리된 주문");
		}else if( "C002".equals(type) ) {
			dto.setType("2014");
			dto.setMessage("요청결제정보가 없습니다.");
		}else if( "A001".equals(type) ) {
			dto.setType("2010");
			dto.setMessage("주문정보가 없음");
		}else if( "C001".equals(type) ) {
			dto.setType("2011");
			dto.setMessage("이미 취소된 주문");
		}else if( "0000".equals(type) ) {
			dto.setType("2010");
			dto.setMessage("결제완료전 주문");
		}else if( "A010".equals(type) ) {
			dto.setType("2010");
			dto.setMessage("주문정보가 없음");
		}else if( "A011".equals(type) ) {
			dto.setType("2014");
			dto.setMessage("주문정보가 없음");
		}else if( "C007".equals(type) ) {
			dto.setType("2014");
			dto.setMessage("카드시스템처리 오류");
		}else if( "C007".equals(type) ) {
			dto.setType("2014");
			dto.setMessage("결제취소오류");
		}else if( "C008".equals(type) ) {
			dto.setType("2014");
			dto.setMessage("취소불가 주문");
		}
		else if( "C009".equals(type) ) {
			dto.setType("2014");
			dto.setMessage("익일입금, 당일입금 - 거래 사용자인 경우, 당일 결제건에서 대해서만 취소가 가능합니다.");
		}
		else if( "C010".equals(type) ) {
			dto.setType("2014");
			dto.setMessage("5일입금 거래 사용자의 경우.결제후 4일까지만 취소가 가능합니다");
		}
		else if( "C011".equals(type) ) {
			dto.setType("2014");
			dto.setMessage("결제정보에 오류가 있습니다. 관리자에게 문의하세요.");
		}
		else if( "C012".equals(type) ) {
			dto.setType("2014");
			dto.setMessage("결제대행사의 잔액이 부족하여 취소가 불가합니다");
		}
		else if( "C013".equals(type) ) {
			dto.setType("2014");
			dto.setMessage("출금 정지된 결제대행사는 취소가 불가합니다. (고객센터:1600-0174)");
		}
		else if( "9999".equals(type) ) {
			dto.setType("9999");
			dto.setMessage("시스템 오류");
		}
		else if( "1010".equals(type) ) {
			dto.setType("1010");
			dto.setMessage("가맹점 거래불가");
		}
		else if( "9002".equals(type) ) {
			dto.setType("9002");
			dto.setMessage("요청항목 누락");
		}
		else if( "9002".equals(type) ) {
			dto.setType("9002");
			dto.setMessage("요청항목 누락");
		}
		// smspay 에서 사용하는 부분
		else if( "SMS01".equals(type) ) {
			dto.setMessage("결제 상품정보가 없습니다.");
		}
		else if( "SMS02".equals(type) ) {
			dto.setMessage("상품정보에 문제가 있습니다.");
		}
		else if( "SMS03".equals(type) ) { // 상품정보 항목에 부정확한 갯수로 입력된 경우 제목 3개 가격2개 이런경우
			dto.setMessage("상품정보가 부정확합니다.");
		}
		else if( "SMS04".equals(type) ) {
			dto.setMessage("이미 결제된 상품입니다.");
		}
		else if( "L001".equals(type) ) { 
			dto.setMessage(" 1회 결제금액 제한");
		}
		else if( "L002".equals(type) ) { 
			dto.setMessage(" 1일 결제금액 제한");
		}
		else if( "L003".equals(type) ) {
			dto.setMessage("1달 결제금액 제한");
		}
		else if( "L004".equals(type) ) {
			dto.setMessage("1년 결제금액 제한");
		}
		else if( "L005".equals(type) ) {
			dto.setMessage("카드번호오류");
		}
		else if( "L006".equals(type) ) {
			dto.setMessage("당일 동일카드 동일 금액 중복결제");
		}
		else if( "L007".equals(type) ) {
			dto.setMessage("결제 상점 설정 오류");
		}
		else if( "L008".equals(type) ) {
			dto.setMessage("결제후 시스템 처리 문제발생 고객센터에 문의 하세요(1600-0174) ");
		}
		else if( "L009".equals(type) ) {
			dto.setMessage("거래가 제한된 판매자 입니다. 고객센터에 문의 하세요(1600-0174) ");
		}
		else if( "S001".equals(type) ) {
			dto.setMessage("sms 상품정보내역 생성에 문제가 있습니다.  고객센터에 문의 하세요(1600-0174)");
		}
		else {
			dto.setType("9999");
			dto.setMessage("시스템오류. 담당자에게 연락해 주세요");
		}
		 return new ResponseEntity<ReturnMsg> (dto,HttpStatus.NOT_EXTENDED);  // 510
	}
	
	@ExceptionHandler({SqlErrorException.class})
	public  ResponseEntity<String> conflict(SqlErrorException e) {
		String temp = e.getMessage();
		if("".equals(temp) || temp == null  ) temp = "데이터처리 문제"; 
		return new ResponseEntity<String> (temp , HttpStatus.UNAUTHORIZED);  
	  }
	// 인증 실패시 / 로그인등 실패시
	@ResponseStatus( HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value = AuthErrorException.class)
	 public ResponseEntity<ReturnMsg> handleAuthException(AuthErrorException e){
		ReturnMsg dto = new ReturnMsg();
		String temp = e.getMessage();
		dto.setType("auth");
		// 로그인 실패시 실패한 항목구분
		if( temp.contains("id") ){ 
			dto.setMessage("아이디를 확인하세요");
		}else if(temp.contains("password")){
			dto.setMessage("비밀번호를 확인하세요");
		}else if(temp.contains("idpw")){
			dto.setMessage("계정정보를 확인하세요");						
		}else if(temp.contains("auth")){
			dto.setMessage("인증에 실패했습니다. 계정을 확인하세요");						
		}else if(temp.contains("no")){
			dto.setMessage("서비스제한 사용자입니다.");						
		}else if(temp.contains("grade")){
			dto.setMessage("권한이 부족한 사용자입니다.");						
		}else {
			dto.setMessage(temp);
		} 
		
        return new ResponseEntity<ReturnMsg> (dto,HttpStatus.UNAUTHORIZED);  
	}
	
	// 인증 실패시 / 로그인등 실패시
	@ResponseStatus( HttpStatus.FORBIDDEN)
	@ExceptionHandler(value = KeyErrorException.class)
	 public ResponseEntity<ReturnMsg> handleKeyException(KeyErrorException e){
		ReturnMsg dto = new ReturnMsg();
		dto.setType("key");
		dto.setMessage(e.getMessage());	
        return new ResponseEntity<ReturnMsg> (dto,HttpStatus.FORBIDDEN);  
	}
	
	// 기존 앱 오류 처리 핸들러
	@ExceptionHandler(AppPreException.class)
	public  ResponseEntity<HashMap<String , String>> appPre(AppPreException e) {
		HashMap<String , String> map = new HashMap<String, String>();
		
		
		if("".equals(e.getCode()) || e.getCode() == null  ) map.put("status", "ERROR");
		else map.put("status", e.getCode());

		if("".equals(e.getMessage()) || e.getMessage() == null  ) map.put("status_detail", "서버처리에러");
		else map.put("status_detail", e.getMessage()); // errorMsg
		// 기존앱에서는 200 으로 응답했다.
		return new ResponseEntity<> (map, HttpStatus.OK);  
	  }
	
	// 디비 입력 수정 등에서 문제가 생긴경우
//	@ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR)
//	@ExceptionHandler(value = SqlErrorException.class)
//	public ResponseEntity<ReturnMsg> handleSqlException(KeyErrorException e){
//		ReturnMsg dto = new ReturnMsg();
//		String temp = e.getMessage();
//		if( temp.contains("sqlerr") ){ 
//			dto.setType("data");
//			dto.setMessage("정보수정이슈");	
//		}
//		
//		dto.setMessage(e.getMessage());	
//        return new ResponseEntity<ReturnMsg> (dto,HttpStatus.INTERNAL_SERVER_ERROR);  
//	}
}
