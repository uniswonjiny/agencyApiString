package org.bizpay.controller;

import java.util.HashMap;
import java.util.List;

import org.bizpay.common.domain.AgencySalesParam;
import org.bizpay.common.domain.DelngCancelParam;
import org.bizpay.common.domain.DelngInsertParam;
import org.bizpay.common.domain.SellerParam;
import org.bizpay.domain.AgencySales;
import org.bizpay.domain.AgencySales2;
import org.bizpay.domain.AgencySales3;
import org.bizpay.domain.DealerInfo;
import org.bizpay.domain.SalesAdjustment;
import org.bizpay.domain.SellerSummary;
import org.bizpay.service.AgencyService;
import org.bizpay.service.AuthService;
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
@RequestMapping("/agency")
@CrossOrigin(origins={"*"})
@Api(tags = "대리점매출관리 ")
public class AgencyRestController {
	@Autowired
	 AgencyService service;
	
	@Autowired
	AuthService aService;
	
	@ApiOperation(value=" 대리점 매출수익" , notes = " 대리점 매출수익 목록")
	@RequestMapping(value = "agency1", method = RequestMethod.POST)
	public ResponseEntity<List<AgencySales>> agency1(@RequestBody AgencySalesParam param) throws Exception{
		log.info("일자별 거래내역조회");
		return new ResponseEntity<>( service.agencySalesList(param), HttpStatus.OK);
	}

	@ApiOperation(value=" 추천 수수료수익" , notes = " 추천 수수료수익 목록")
	@RequestMapping(value = "agency2", method = RequestMethod.POST)
	public ResponseEntity<List<AgencySales2>> agency2(@RequestBody AgencySalesParam param) throws Exception{
		log.info("일자별 거래내역조회");
		return new ResponseEntity<>( service.agencySalesList2(param), HttpStatus.OK);
	}

	@ApiOperation(value=" 가맹비 수익" , notes = "가맹비 수익 목록")
	@RequestMapping(value = "agency3", method = RequestMethod.POST)
	public ResponseEntity<List<AgencySales3>> agency3(@RequestBody AgencySalesParam param) throws Exception{
		log.info("일자별 거래내역조회");
		return new ResponseEntity<>( service.agencySalesList3(param), HttpStatus.OK);
	}
	
	@ApiOperation(value=" 판매자 매출" , notes = "판매자 매출")
	@RequestMapping(value = "seller", method = RequestMethod.POST)
	public ResponseEntity<List<SellerSummary>> seller(@RequestBody SellerParam param) throws Exception{
		log.info("일자별 거래내역조회");
		return new ResponseEntity<>( service.sellerSummaryList(param), HttpStatus.OK);
	}
	
	@ApiOperation(value="매출조정목록" , notes = "매출조정조회")
	@RequestMapping(value = "salesAdjustment", method = RequestMethod.POST)
	public ResponseEntity<List<SalesAdjustment>> salesAdjustment(@RequestBody SellerParam param) throws Exception{
		log.info("매출조정조회");
		return new ResponseEntity<>( service.salesAdjustment(param), HttpStatus.OK);
	}
	
	@ApiOperation(value="매출취소" , notes = "매출취소")
	@RequestMapping(value = "cancelAdjustment", method = RequestMethod.POST)
	public ResponseEntity<HashMap< String, Object>> cancelAdjustment(@RequestBody DelngCancelParam param) throws Exception{
		log.info("매출취소");
		return new ResponseEntity<>( service.delngCancel(param), HttpStatus.OK);
	}

	@ApiOperation(value="매출 일괄입력" , notes = "매출 일괄입력")
	@RequestMapping(value = "salesListInsert", method = RequestMethod.POST)
	public ResponseEntity<HashMap<String, Object>> salesListInsert(@RequestBody List<DelngInsertParam> list) throws Exception{
		log.info("매출 일괄입력");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("flag", true);
		map.put("message", "");
		// 권한검사 -- 인증세션기능개발후 바꾼다.
		if(list.size() <1) {
			map.put("flag", false);
			map.put("message", "입력데이터 없음");
			return new ResponseEntity<>( map, HttpStatus.ACCEPTED);
		}
		
		if(!("AUTHOR_DEALER".equals( list.get(0).getGrade()) || "AUTHOR_MNGR".equals(list.get(0).getGrade()))) {
			map.put("flag", false);
			map.put("message", "권한없음");
			return new ResponseEntity<>( map, HttpStatus.ACCEPTED);
		}
		
		return new ResponseEntity<>( null, HttpStatus.OK);
	}
	
	// 대리점 전체 목록
	@ApiOperation(value="대리점 정보 목록" , notes = "대리점 전체 목록")
	@RequestMapping(value = "dealerList", method = RequestMethod.POST)
	public ResponseEntity<List<DealerInfo>> dealerList() throws Exception{
		log.info("이용가능한 전체 대리점 목록");
		List<DealerInfo> dealerList = aService.dealerList("0000002");
		return new ResponseEntity<>( dealerList, HttpStatus.OK);
	}
}
