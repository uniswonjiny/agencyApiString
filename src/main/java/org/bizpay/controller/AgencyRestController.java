package org.bizpay.controller;

import java.util.List;

import org.bizpay.common.domain.AgencySalesParam;
import org.bizpay.domain.AgencySales;
import org.bizpay.service.AgencyService;
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
@RequestMapping("/agency")
@CrossOrigin(origins={"*"})
public class AgencyRestController {
	@Autowired
	 AgencyService service;
	
	// 대리점매출1
	@RequestMapping(value = "agency", method = RequestMethod.POST)
	public ResponseEntity<List<AgencySales>> transByDay(@RequestBody AgencySalesParam param) throws Exception{
		log.info("일자별 거래내역조회");
		return new ResponseEntity<>( service.agencySalesList(param), HttpStatus.OK);
	}
	
	
}
