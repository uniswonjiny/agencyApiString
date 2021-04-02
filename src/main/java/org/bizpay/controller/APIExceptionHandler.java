package org.bizpay.controller;

import java.sql.SQLException;
import java.util.HashMap;

import org.bizpay.domain.ReturnMsg;
import org.bizpay.exception.AppPreException;
import org.bizpay.exception.AuthErrorException;
import org.bizpay.exception.KeyErrorException;
import org.bizpay.exception.SqlErrorException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class APIExceptionHandler {
	// exception.class 처리
	@ExceptionHandler({SQLException.class,DataAccessException.class ,Exception.class })
	public  ResponseEntity<String> sqlNormal(SQLException e) {
		return new ResponseEntity<String> ("서버등에 문제가 있습니다. 잠시후 이용해주세요" , HttpStatus.UNAUTHORIZED);  
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
