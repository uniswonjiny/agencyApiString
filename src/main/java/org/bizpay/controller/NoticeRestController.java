package org.bizpay.controller;

import java.util.List;

import org.bizpay.domain.Notice;
import org.bizpay.domain.PgFee;
import org.bizpay.service.NoticeService;
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
@RequestMapping("/notice")
@CrossOrigin(origins={"*"})
public class NoticeRestController {
	@Autowired
	NoticeService nService;
	
	// 공지사항 목록
	@RequestMapping(value = "noticeList", method = RequestMethod.GET)
	public ResponseEntity<List<Notice>> agencyList() throws Exception{
		log.info("공지사항 목록");
		return new ResponseEntity<>(nService.noticeList(),   HttpStatus.OK);
	}
	// 수수료율 목록
	@RequestMapping(value = "pgFeeList", method = RequestMethod.GET)
	public ResponseEntity<List<PgFee>> pgFeeList() throws Exception{
		log.info("pg 수수료율 목록");
		return new ResponseEntity<>(nService.pgFeeList(), HttpStatus.OK);
	}
}