package org.bizpay.controller;

import java.util.HashMap;
import java.util.List;

import org.bizpay.common.domain.AgencyManageParam;
import org.bizpay.domain.AgencyManage;

import org.bizpay.service.AgencyManageSevice;
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
@RequestMapping("/manage")
@CrossOrigin(origins={"*"})
public class AgencyManageController {
	@Autowired
	AgencyManageSevice serve;
	
	// 대리점 목록
	@RequestMapping(value = "agencyList", method = RequestMethod.POST)
	public ResponseEntity<List<AgencyManage>> agencyList(@RequestBody AgencyManageParam param) throws Exception{
		log.info("대리점목록");
		return new ResponseEntity<>(serve.agencyList(param),   HttpStatus.OK);
	}
	// 대리점 정보 수정
	@RequestMapping(value = "updateAgency", method = RequestMethod.POST)
	public ResponseEntity<String> agencyUpdate(@RequestBody AgencyManageParam param) throws Exception{
		log.info("대리점정보 수정");
		HashMap< String, Object> map =  serve.upatgeAgency(param);
		if( (boolean) map.get("flag") ) {
			return new ResponseEntity<>("",   HttpStatus.OK);
		}else 	return new ResponseEntity<>(map.get("message").toString() ,   HttpStatus.BAD_GATEWAY);
	}
	
	// 대리점 정보 수정
		@RequestMapping(value = "insertAgency", method = RequestMethod.POST)
		public ResponseEntity<String> agencyInsert(@RequestBody AgencyManageParam param) throws Exception{
			log.info("대리점정보 입력");
			HashMap< String, Object> map =  serve.upatgeAgency(param);
			if( (boolean) map.get("flag") ) {
				return new ResponseEntity<>("",   HttpStatus.OK);
			}else 	return new ResponseEntity<>(map.get("message").toString() ,   HttpStatus.BAD_GATEWAY);
		}
}
