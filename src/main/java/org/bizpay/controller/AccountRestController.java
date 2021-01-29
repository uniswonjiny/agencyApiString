package org.bizpay.controller;

import java.util.List;

import org.bizpay.common.domain.AccountExcelParam;
import org.bizpay.common.domain.AccountListParam;
import org.bizpay.common.domain.AccountTransParam;
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
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/account")
@CrossOrigin(origins={"*"})
public class AccountRestController {
	@Autowired
	AccountService aService;
	
	// 입출력내역관리 목록
	@RequestMapping(value = "accountList", method = RequestMethod.POST)
	public ResponseEntity<List<AccountInOut>> agencyList(@RequestBody AccountListParam param) throws Exception{
		log.info("대리점목록");
		return new ResponseEntity<>(aService.AccountInList(param),   HttpStatus.OK);
	}
	// 정산엑셀
	@RequestMapping(value = "accountExcelList", method = RequestMethod.POST)
	public ResponseEntity<List<AccountExcel>> accountExcel(@RequestBody AccountExcelParam param) throws Exception{
		log.info("정산엑셀");
		return new ResponseEntity<>(aService.accountExcelList(param),   HttpStatus.OK);
	}
	// 계좌 이체조정목록
	@RequestMapping(value = "accountTransList", method = RequestMethod.POST)
	public ResponseEntity<List<AccountTrans>> accountTransList(@RequestBody AccountTransParam param) throws Exception{
		log.info("계좌이체 조정목록");
		return new ResponseEntity<>(aService.transferList(param),   HttpStatus.OK);
	}
	
	// 은행계좌이체목록
	@RequestMapping(value = "bankAcntTransList", method = RequestMethod.POST)
	public ResponseEntity<List<BankAcntTrans>> bankAcntTransList(@RequestBody BankAcntTransParam param) throws Exception{
		log.info("은행계좌이체목록");
		return new ResponseEntity<>(aService.bankAcntTransList(param),   HttpStatus.OK);
	}
	
}
