package org.bizpay.controller;

import java.util.HashMap;
import java.util.List;

import org.bizpay.common.domain.AccountExcelParam;
import org.bizpay.common.domain.AccountListParam;
import org.bizpay.common.domain.AccountTransParam;
import org.bizpay.common.domain.AcnutTransfrYnParam;
import org.bizpay.common.domain.BankAcntTransParam;
import org.bizpay.domain.AccountExcel;
import org.bizpay.domain.AccountInOut;
import org.bizpay.domain.AccountTrans;
import org.bizpay.domain.BankAcntTrans;
import org.bizpay.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/account")
@CrossOrigin(origins={"*"})
@Api(tags = "계좌정보 Account")
public class AccountRestController {
	@Autowired
	AccountService aService;
	

	@ApiOperation(value="입출력내역관리 목록" , notes = "입출력내역목록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "dateStart" , value = "검색시작일" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "dateEnd" , value = "검색종료일" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "memberBizeCode" , value = "로그인한유저대리점코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizCode" , value = "대리점코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "inoutCode" , value = "거래구분코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "mberName" , value = "판매자성명" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "indutyId" , value = "판매자 아이디" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizNum" , value = "사업자 번호" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "companyName" , value = "상호" , required = false , dataType = "string" , paramType = "query" ),
	})
	@RequestMapping(value = "accountList", method = RequestMethod.POST)
	public ResponseEntity<List<AccountInOut>> agencyList(@RequestBody AccountListParam param) throws Exception{
		log.info("대리점목록");
		return new ResponseEntity<>(aService.AccountInList(param),   HttpStatus.OK);
	}

	@ApiOperation(value="정산엑셀" , notes = "정산엑셀목록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "dateStart" , value = "검색시작일" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "dateEnd" , value = "검색종료일" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "memberBizeCode" , value = "로그인한유저대리점코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizCode" , value = "대리점코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizrno" , value = "사업자번호" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "mberName" , value = "판매자성명" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "indutyId" , value = "판매자 아이디" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizNum" , value = "사업자 번호" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "companyName" , value = "상호" , required = false , dataType = "string" , paramType = "query" ),
	})
	@RequestMapping(value = "accountExcelList", method = RequestMethod.POST)
	public ResponseEntity<List<AccountExcel>> accountExcel(@RequestBody AccountExcelParam param) throws Exception{
		log.info("정산엑셀");
		return new ResponseEntity<>(aService.accountExcelList(param),   HttpStatus.OK);
	}

	@ApiOperation(value="계좌 이체조정목록" , notes = "계좌 이체조정목록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "dateStart" , value = "검색시작일" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "dateEnd" , value = "검색종료일" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "memberBizeCode" , value = "로그인한유저대리점코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizCode" , value = "대리점코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizrno" , value = "사업자번호" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "mberName" , value = "판매자성명" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "indutyId" , value = "판매자 아이디" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "confmNo" , value = "승인 번호" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "pymntYn" , value = "Y / N" , required = false , dataType = "string" , paramType = "query" ),
	})
	@RequestMapping(value = "accountTransList", method = RequestMethod.POST)
	public ResponseEntity<List<AccountTrans>> accountTransList(@RequestBody AccountTransParam param) throws Exception{
		log.info("계좌이체 조정목록");
		return new ResponseEntity<>(aService.transferList(param),   HttpStatus.OK);
	}

	@ApiOperation(value="은행계좌이체목록" , notes = "은행계좌이체목록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "dateStart" , value = "검색시작일" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "dateEnd" , value = "검색종료일" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "memberBizeCode" , value = "로그인한유저대리점코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizCode" , value = "대리점코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizrno" , value = "사업자번호" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "mberName" , value = "판매자성명" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "indutyId" , value = "판매자 아이디" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "payType" , value = "코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "pymntYn" , value = "Y / N" , required = false , dataType = "string" , paramType = "query" ),
	})
	@RequestMapping(value = "bankAcntTransList", method = RequestMethod.POST)
	public ResponseEntity<List<BankAcntTrans>> bankAcntTransList(@RequestBody BankAcntTransParam param) throws Exception{
		log.info("은행계좌이체목록");
		return new ResponseEntity<>(aService.bankAcntTransList(param),   HttpStatus.OK);
	}
	

	@ApiOperation(value="출금정지설정" , notes = "inOutSetting 출금정지설정")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "enableYn" , value = "출금가능 Y  출금불가 N" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "msg" , value = "메세지" , required = false , dataType = "string" , paramType = "query" ),
	})
	@RequestMapping(value = "inOutSetting", method = RequestMethod.POST)
	public ResponseEntity<String> inOutSetting(@RequestParam String enableYn , @RequestParam String msg) throws Exception{
		log.info("출금정지설정");
		HashMap<String, Object> map = new HashMap<>();
		map.put("enableYn", enableYn);
		map.put("msg", msg);
		if(aService.inOutSetting(map) <1) {
			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		}else {
			return new ResponseEntity<>( "",HttpStatus.OK);
		}
	}
	
	@ApiOperation(value="출금정지상태" , notes = "출금정지상태")
	@RequestMapping(value = "inOutSettingInfo", method = RequestMethod.GET)
	public ResponseEntity<HashMap<String, Object>> inOutSettingInfo() throws Exception{
		log.info("출금정지상태");			
		return new ResponseEntity<>(aService.inOutSettingInfo(),  HttpStatus.OK);
	}

	@ApiOperation(value="계좌이체조정 지급, 미지급처리 목록" , notes = "계좌이체조정 목록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pymntYn" , value = "Y / N" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "mberCode" , value = "사용자코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "mberCodeSn" , value = "사용자코드순번" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "rciptNo" , value = "영수증번호" , required = false , dataType = "string" , paramType = "query" ),
	})
	@RequestMapping(value = "acnutTransfrYn", method = RequestMethod.POST)
	public ResponseEntity<String> acnutTransfrYn(@RequestBody List<AcnutTransfrYnParam> param) throws Exception{
		log.info("계좌이체조정 지급, 미지급처리 - param" + param.toString());			
		//return new ResponseEntity<>(HttpStatus.OK);
		if(aService.acnutTransfrYn(param)>0 ) {
			return new ResponseEntity<>("",HttpStatus.OK);
		}else return new ResponseEntity<>("",HttpStatus.OK);
	}
	
}
