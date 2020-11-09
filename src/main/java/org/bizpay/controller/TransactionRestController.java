package org.bizpay.controller;

import java.util.List;

import org.bizpay.common.domain.InqireDelingParam;
import org.bizpay.domain.TransByDate;
import org.bizpay.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/trans")
@CrossOrigin(origins={"*"})
public class TransactionRestController {
	@Autowired
	TransactionService tService;
	
	// 일자별 거래내역
	@RequestMapping(value = "transByDay", method = RequestMethod.POST)
	public ResponseEntity<List<TransByDate>> transByDay(@RequestBody InqireDelingParam param) throws Exception{
		log.info("일자별 거래내역조회");
		return null;
	}
}
