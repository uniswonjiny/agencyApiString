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

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/account")
@CrossOrigin(origins={"*"})
public class AccountRestController {
	@Autowired
	AccountService aService;
	
	@RequestMapping(value = "accountList", method = RequestMethod.POST)
	public ResponseEntity<List<AccountInOut>> agencyList(@RequestBody AccountListParam param) throws Exception{
		log.info("대리점목록");
		return new ResponseEntity<>(aService.AccountInList(param),   HttpStatus.OK);
	}

	@RequestMapping(value = "accountExcelList", method = RequestMethod.POST)
	public ResponseEntity<List<AccountExcel>> accountExcel(@RequestBody AccountExcelParam param) throws Exception{
		log.info("정산엑셀");
		return new ResponseEntity<>(aService.accountExcelList(param),   HttpStatus.OK);
	}

	@RequestMapping(value = "accountTransList", method = RequestMethod.POST)
	public ResponseEntity<List<AccountTrans>> accountTransList(@RequestBody AccountTransParam param) throws Exception{
		log.info("계좌이체 조정목록");
		return new ResponseEntity<>(aService.transferList(param),   HttpStatus.OK);
	}

	@RequestMapping(value = "bankAcntTransList", method = RequestMethod.POST)
	public ResponseEntity<List<BankAcntTrans>> bankAcntTransList(@RequestBody BankAcntTransParam param) throws Exception{
		log.info("은행계좌이체목록");
		return new ResponseEntity<>(aService.bankAcntTransList(param),   HttpStatus.OK);
	}
	
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
	
	@RequestMapping(value = "inOutSettingInfo", method = RequestMethod.GET)
	public ResponseEntity<HashMap<String, Object>> inOutSettingInfo() throws Exception{
		log.info("출금정지상태");			
		return new ResponseEntity<>(aService.inOutSettingInfo(),  HttpStatus.OK);
	}

	@RequestMapping(value = "acnutTransfrYn", method = RequestMethod.POST)
	public ResponseEntity<String> acnutTransfrYn(@RequestBody List<AcnutTransfrYnParam> param) throws Exception{
		log.info("계좌이체조정 지급, 미지급처리 - param" + param.toString());			
		//return new ResponseEntity<>(HttpStatus.OK);
		if(aService.acnutTransfrYn(param)>0 ) {
			return new ResponseEntity<>("",HttpStatus.OK);
		}else return new ResponseEntity<>("",HttpStatus.OK);
	}
}
