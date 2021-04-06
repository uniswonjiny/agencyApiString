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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/manage")
@CrossOrigin(origins={"*"})
@Api(tags = "대리점관리-AgencyManage ")
public class AgencyManageController {
	@Autowired
	AgencyManageSevice serve;
	
	@ApiOperation(value=" 대리점 목록" , notes = " 대리점 목록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "cmpnm" , value = "상호" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "dealerId" , value = "대리점 아아디" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "indutyId" , value = "로그인한 아이디" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "useAt" , value = "거래구분" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizNum" , value = "사업자번호" , required = false , dataType = "string" , paramType = "query" ),
	})
	@RequestMapping(value = "agencyList", method = RequestMethod.POST)
	public ResponseEntity<List<AgencyManage>> agencyList(@RequestBody AgencyManageParam param) throws Exception{
		log.info("대리점목록");
		return new ResponseEntity<>(serve.agencyList(param),   HttpStatus.OK);
	}

	@ApiOperation(value=" 대리점 정보 수정" , notes = "대리점 정보 수정")
	@RequestMapping(value = "updateAgency", method = RequestMethod.POST)
	public ResponseEntity<Void> agencyUpdate(@RequestBody AgencyManageParam param) throws Exception{
		log.info("대리점정보 수정");
		serve.upatgeAgency(param); // 에러처리는 핸들러에서 하므로 실행만 하면된다.
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(value=" 기준대리점정보획득" , notes = "기준대리점정보획득")
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
	
	@ApiOperation(value=" 대리점 정보 입력" , notes = "대리점 정보 입력")
	@RequestMapping(value = "insertAgency", method = RequestMethod.POST)
	public ResponseEntity<Void> agencyInsert(@RequestBody AgencyManageParam param) throws Exception{
		log.info("대리점정보 입력");
		return new ResponseEntity<>(HttpStatus.OK);
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
		return new ResponseEntity<>("ok",   HttpStatus.OK);
	}
	
	// 판매자 수정
	@RequestMapping(value = "updateSeller", method = RequestMethod.POST)
	public ResponseEntity<HashMap< String, Object>> updateList(@RequestBody SellerInsertParam param) throws Exception{
		log.info("대리점입력");
		serve.updateSeller(param);
		return new ResponseEntity<>(HttpStatus.OK);
	}	
}
