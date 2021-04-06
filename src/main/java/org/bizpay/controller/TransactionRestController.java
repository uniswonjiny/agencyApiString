package org.bizpay.controller;

import java.util.List;
import org.bizpay.common.domain.InqireDelingParam;
import org.bizpay.domain.InqireDelng;
import org.bizpay.domain.TransByDate;
import org.bizpay.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/trans")
@CrossOrigin(origins={"*"})
@Api(tags = "거래내역")
public class TransactionRestController {
	@Autowired
	TransactionService tService;
	
	@ApiOperation(value="일자별 거래내역" , notes = "일자별 거래내역 조회")
	@RequestMapping(value = "transByDay", method = RequestMethod.POST)
	public ResponseEntity<List<TransByDate>> transByDay(@RequestBody InqireDelingParam param) throws Exception{
		log.info("일자별 거래내역조회");
		return new ResponseEntity<>( tService.TransByDateList(param), HttpStatus.OK);
	}
	
	@ApiOperation(value="거래내역조회" , notes = "거래내역조회 and 취소내역조회")
	@RequestMapping(value = "transListSearch", method = RequestMethod.POST)
	public ResponseEntity<List<InqireDelng>> transSearch(@RequestBody InqireDelingParam param) throws Exception{
		log.info("검색기간별 거래내역조회 ");
		log.info("============================================================================================= ");
		log.info(param.toString());
		log.info("============================================================================================= ");
		return new ResponseEntity<>( tService.transSearchList(param), HttpStatus.OK);
	}
	
}
