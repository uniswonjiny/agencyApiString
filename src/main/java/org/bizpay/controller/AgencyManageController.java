package org.bizpay.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bizpay.common.domain.AgencyManageParam;
import org.bizpay.common.domain.SellerInsertParam;
import org.bizpay.common.domain.SellerManageParam;
import org.bizpay.domain.AgencyManage;
import org.bizpay.domain.SellerList;
import org.bizpay.service.AgencyManageSevice;
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
	
	// 기준대리점정보획득
	@RequestMapping(value = "settingAgencyList/{memberCode}/{agencyCode}", method = RequestMethod.GET)
	public ResponseEntity<HashMap< String, Object>> settingAgencyList(@PathVariable("memberCode") String memberCode ,@PathVariable("agencyCode") String agencyCode ) throws Exception{
		log.info("기준대리점정보획득");
		HashMap< String, Object> map = new HashMap<String, Object>();
		List<HashMap< String, Object>> agencyKindList =  serve.settingAgencyList(memberCode ,agencyCode );
		List<HashMap< String, Object>> settingAgencyList =  serve.settingAgencyList2(memberCode ,agencyCode );
		map.put("agencyKindList", agencyKindList);
		map.put("settingAgencyList", settingAgencyList);
		return new ResponseEntity<>(map,   HttpStatus.OK);
	
	}
	
	// 대리점 정보 입력
	@RequestMapping(value = "insertAgency", method = RequestMethod.POST)
	public ResponseEntity<String> agencyInsert(@RequestBody AgencyManageParam param) throws Exception{
		log.info("대리점정보 입력");
		//////// 중요 리턴 부분은 에러 핸들러에서 에러 생겼을 때 응답 하도록 바꿀예정임 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		HashMap< String, Object> map =  serve.insertAgency(param);
		if( (boolean) map.get("flag") ) {
			return new ResponseEntity<>("",   HttpStatus.OK);
		}else 	return new ResponseEntity<>(map.get("message").toString() ,   HttpStatus.BAD_GATEWAY);
	}
	
	// 판매자관리목록
	@RequestMapping(value = "sellerList", method = RequestMethod.POST)
	public ResponseEntity<List<SellerList>> sellerList(@RequestBody SellerManageParam param) throws Exception{
		log.info("대리점목록");
		return new ResponseEntity<>(serve.selectSellerList(param),   HttpStatus.OK);
	}
	// 판매자 등록
	@RequestMapping(value = "insertSeller", method = RequestMethod.POST)
	public ResponseEntity<String> sellerList(@RequestBody List<SellerInsertParam> param) throws Exception{
		log.info("대리점입력");
		System.out.println(param.toString());
//		return new ResponseEntity<>("ok",   HttpStatus.OK);
		if(serve.insertSellerList(param) > 0)return new ResponseEntity<>("ok",   HttpStatus.OK);
		else return new ResponseEntity<>("fail",   HttpStatus.OK);
	}
	
	// 판매자 수정
	@RequestMapping(value = "updateSeller", method = RequestMethod.POST)
	public ResponseEntity<HashMap< String, Object>> updateList(@RequestBody SellerInsertParam param) throws Exception{
		log.info("대리점입력");
		return new ResponseEntity<>(serve.updateSeller(param),   HttpStatus.OK);
	}	
}
