package org.bizpay.controller;

import java.util.List;

import org.bizpay.domain.Notice;
import org.bizpay.domain.PgFee;
import org.bizpay.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;

// !!!!!!!!!!!!!!! exception handler 작성후 ResponseEntity 리턴 부분 모두 수정해야한다.!!!!!!!!!!!!!!!!!!!!!!!!!!!


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

	// 공지사항목록 삭제 http://localhost:8080/deleteNotice/1,2,3,4
	@RequestMapping(value = "deleteNotice/{idxList}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteNotice(@PathVariable List<Integer> idxList) throws Exception{
		log.info("공지사항 삭제");
		// sql 에러 처리 핸들러 작성후 핸들러에서 처리하도록 추후 변경한다.
		log.info(idxList.toString());
		if(nService.deleteNotice(idxList) <1 )return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		//if(true )return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		else return new ResponseEntity<>( HttpStatus.OK);
	}
	// 공지사항저장 
	@RequestMapping(value = "saveNotice", method = RequestMethod.POST)
	public ResponseEntity<String> saveNotice(@RequestBody Notice notice) throws Exception{
		log.info("공지사항 저장");
		if(nService.saveNotice(notice) <1 )return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		else return new ResponseEntity<>( HttpStatus.OK);
	}
	// 공지사항수정 
	@RequestMapping(value = "updateNotice", method = RequestMethod.POST)
	public ResponseEntity<String> updateNotice(@RequestBody Notice notice) throws Exception{
		log.info("공지사항 수정");
		if(nService.updateNotice(notice) <1 )return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		else return new ResponseEntity<>( HttpStatus.OK);
	}
	
	// 수수료율 목록
	@RequestMapping(value = "pgFeeList", method = RequestMethod.GET)
	public ResponseEntity<List<PgFee>> pgFeeList() throws Exception{
		log.info("pg 수수료율 목록");
		return new ResponseEntity<>(nService.pgFeeList(), HttpStatus.OK);
	}
	
	// 수수료율 저장 
	@RequestMapping(value = "savePgFee", method = RequestMethod.POST)
	public ResponseEntity<String> savePgFee(@RequestBody PgFee pgFee) throws Exception{
		log.info("공지사항 저장");
		if(nService.savePgFee(pgFee) <1 )return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		else return new ResponseEntity<>( HttpStatus.OK);
	}
	// 수수료율 수정 
	@RequestMapping(value = "updatePgFee", method = RequestMethod.POST)
	public ResponseEntity<String> updatePgFee(@RequestBody PgFee pgFee) throws Exception{
		log.info("공지사항 수정");
		if(nService.updatePgFee(pgFee) <1 )return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		else return new ResponseEntity<>( HttpStatus.OK);
	}
	
	// 수수료율삭제 http://localhost:8080/deleteNotice/1,2,3,4
	@RequestMapping(value = "deletePgFee/{idxList}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deletePgFee(@PathVariable List<Integer> idxList) throws Exception{
		log.info("수수료율 삭제");
		if(nService.deletePgFee(idxList) <1 )return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		else return new ResponseEntity<>( HttpStatus.OK);
	}
}