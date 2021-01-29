package org.bizpay.controller;

import java.util.List;

import org.bizpay.common.domain.GuarantParam;
import org.bizpay.common.domain.TaxIssueParam;
import org.bizpay.common.domain.TaxReportParam;
import org.bizpay.domain.Guarant;
import org.bizpay.domain.TaxIssue;
import org.bizpay.domain.TaxReport;
import org.bizpay.service.TaxService;
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
@RequestMapping("/tax")
@CrossOrigin(origins={"*"})
public class TaxRestController {
	@Autowired
	TaxService tService;
	
	@RequestMapping(value = "taxIssueList", method = RequestMethod.POST)
	public ResponseEntity<List<TaxIssue>>  taxIssueList(@RequestBody TaxIssueParam param) throws Exception {
		log.info("세금계산서 발급목록 조회");
		return new ResponseEntity<>(tService.taxIssueList(param), HttpStatus.OK);
	}
	@RequestMapping(value = "taxReportList", method = RequestMethod.POST)
	public ResponseEntity<List<TaxReport>>  taxReportList(@RequestBody TaxReportParam param) throws Exception {
		log.info("세무서 신고목록 조회");
		return new ResponseEntity<>(tService.taxReportList(param), HttpStatus.OK);
	}
	@RequestMapping(value = "guarantList", method = RequestMethod.POST)
	public ResponseEntity<List<Guarant>> guarantList(@RequestBody GuarantParam param) throws Exception {
		log.info("보증료 목록 조회");
		return new ResponseEntity<>(tService.guarantList(param), HttpStatus.OK);
	}
}
