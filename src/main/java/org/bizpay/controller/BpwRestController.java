package org.bizpay.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bizpay.common.domain.InqireDealerParam;
import org.bizpay.common.domain.InqireDelingParam;
import org.bizpay.common.domain.InqireMberParam;
import org.bizpay.domain.AtmConfig;
import org.bizpay.domain.DealerManager;
import org.bizpay.domain.DealerRegInfo;
import org.bizpay.domain.InqireAtm;
import org.bizpay.domain.InqireAtmSum;
import org.bizpay.domain.InqireDelng;
import org.bizpay.domain.InqireDelngSum;
import org.bizpay.service.BPWService;
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
@RequestMapping("/bpw")
@CrossOrigin(origins={"*"})
@Api(tags = "입출금내역 ")
public class BpwRestController {
	@Autowired
	BPWService bService;
	
	// 입출금내역
	@ApiOperation(value="입출금목록" , notes = "입출금내역조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "bizCode" , value = "비즈코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "memberBizeCode" , value = "로그인한유저 비즈코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "memberMberCode" , value = "로그인한 유저 사용자코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "dateStart" , value = "등록일자시작" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "dateEnd" , value = "등록일자종료" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "mberName" , value = " 판매자성명" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "indutyId" , value = "판매자 아이디" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizNum" , value = "사업자 번호" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "useAt" , value = "거래구분" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "calculateType" , value = "정산구분" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "depositor" , value = "예금주" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "nm" , value = "상호" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizTypeName" , value = "업종" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizItem" , value = "취급품목" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "inoutCode" , value = "입출금코드" , required = false , dataType = "string" , paramType = "query" ),
	})
	@RequestMapping(value = "InqireAtm", method = RequestMethod.POST)
	public ResponseEntity<Map<String , Object>>  InqireATM(@RequestBody InqireMberParam param) throws Exception {
		log.info("입출금내역페이징 조회");
		
		Map<String, Object> map = new HashMap<>();
		
		List<InqireAtm> list = bService.InqireAtm(param);
		InqireAtmSum sum = bService.InqireAtmTot(param);
		map.put("list", list);
		map.put("sum", sum);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	// 거래내역
	@ApiOperation(value="거래내역목록" , notes = "거래내역")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "dateStart" , value = "등록일자시작" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "dateEnd" , value = "등록일자종료" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizCode" , value = "비즈코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "memberBizeCode" , value = "로그인한유저 비즈코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "mberName" , value = " 판매자성명" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "tid" , value = "단말기 아이디 스와이프 구분용 스와이프 순간 , D+2" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "delngSeCode" , value = "거래구분코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "calculateType" , value = "정산구분" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "mberName" , value = "판매자성명" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "confmNo" , value = "승인번호" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "mberId" , value = "판매자 아이디" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizNum" , value = "사업자 번호" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "issueCmpnyNm" , value = "카드사" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "sellPrice" , value = "판매금액" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "badCardSearch" , value = "동일카드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "delngPayType" , value = "익일 바로정산 구분 " , required = false , dataType = "string" , paramType = "query" ),
	})
	@RequestMapping(value = "InqireDelng", method = RequestMethod.POST)
	public ResponseEntity<Map<String , Object>>  inqireDelng(@RequestBody InqireDelingParam param) throws Exception {
		log.info("입출금내역 전체 조회");
		int totCount = 0;
		Map<String, Object> map = new HashMap<>();
		List<InqireDelng> list = bService.inqireDelng(param);
		InqireDelngSum sum = bService.inqireDelngSum(param);
		if(param.getEndIndex()>0 ) {			
			totCount =  bService.inqireDelngCount(param);
		}
		map.put("list", list);
		map.put("sum", sum);
		map.put("totCount", totCount);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@ApiOperation(value="출금정지상태 확인" , notes = "출금정지상태 확인")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "gbCode" , value = "선택한 코드 " , required = false , dataType = "string" , paramType = "query" ),
	})
	@RequestMapping(value = "atmConfig/{gbCode}", method = RequestMethod.GET)
	public ResponseEntity<AtmConfig> atmConfigInfo(@PathVariable("gbCode") String gbCode)  throws Exception {
		log.info("출금정지 상태");
		AtmConfig ac = bService.atmConfigInfo(gbCode);
		return new ResponseEntity<>(ac, HttpStatus.OK);
	}

	@ApiOperation(value="출금정지상태 목록" , notes = "출금정지상태 변경")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "msg" , value = "메세지" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "enableYn" , value = "사용유무" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "gbCode" , value = "선택한 코드 " , required = false , dataType = "string" , paramType = "query" ),
	})
	@RequestMapping(value = "atmConfig", method = RequestMethod.POST)
	public ResponseEntity<String> atmConfigInfo(@RequestBody AtmConfig param)  throws Exception {
		log.info("출금정지상태변경");
		if(bService.atmConfigUpdate(param)) return new ResponseEntity<>("ok", HttpStatus.OK);
		else return new ResponseEntity<>("fail", HttpStatus.ACCEPTED);//202  요청은 응했지만 실행되지 않았다.
	}
	
	@ApiOperation(value="대리점관리용목록" , notes = "대리점관리목록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "dateStart" , value = "등록일자시작" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "dateEnd" , value = "등록일자종료" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizCode" , value = "비즈코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "memberBizeCode" , value = "로그인한유저 비즈코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "mberName" , value = " 판매자성명" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "tid" , value = "단말기 아이디 스와이프 구분용 스와이프 순간 , D+2" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "delngSeCode" , value = "거래구분코드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "calculateType" , value = "정산구분" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "mberName" , value = "판매자성명" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "confmNo" , value = "승인번호" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "mberId" , value = "판매자 아이디" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "bizNum" , value = "사업자 번호" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "issueCmpnyNm" , value = "카드사" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "sellPrice" , value = "판매금액" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "badCardSearch" , value = "동일카드" , required = false , dataType = "string" , paramType = "query" ),
		@ApiImplicitParam(name = "delngPayType" , value = "익일 바로정산 구분 " , required = false , dataType = "string" , paramType = "query" ),
	})
	@RequestMapping(value = "InqireDealer", method = RequestMethod.POST)
	public ResponseEntity<Map<String , Object>> inqireDealer(@RequestBody InqireDealerParam param) throws Exception {
		log.info("대리점관리 목록");
		Map<String, Object> map = new HashMap<>();
		List<DealerManager> list = bService.inqireDealer(param);
		int totCount = bService.inqireDealerCount(param);
		map.put("list", list);
		map.put("totCount", totCount);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	@ApiOperation(value="대리점 상세정보확인" , notes = "개별대리점 상세정보확인")
	@RequestMapping(value = "dealerbyId/{userId}", method = RequestMethod.GET)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId" , value = "사용자아이디" , required = false , dataType = "string" , paramType = "query" ),
	})
	public ResponseEntity<DealerRegInfo> dealerbyId(@PathVariable("userId") String userId)  throws Exception {
		log.info("개별대리점 상세정보 확인");
		DealerRegInfo info = bService.dealerbyId(userId);
		return new ResponseEntity<>(info, HttpStatus.OK);
	}

	@ApiOperation(value="대리점신규입력" , notes = "대리점정보생성")
	@RequestMapping(value = "dealerCreate", method = RequestMethod.POST)
	public ResponseEntity<String> dealerCreate(@RequestBody DealerRegInfo dealerInfo)  throws Exception {
		log.info("신규대리점정보 입력");
		//DealerRegInfo info = bService.dealerbyId(userId);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
	
	@ApiOperation(value="대리점정보수정" , notes = "대리점정보수정")
	@RequestMapping(value = "dealerModify", method = RequestMethod.POST)
	public ResponseEntity<String> dealerUpdate(@RequestBody  DealerRegInfo dealerInfo)  throws Exception {
		log.info("대리점정보수정");
		bService.updateDealer(dealerInfo);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
	
	@ApiOperation(value="딜러정보 중복확인" , notes = "딜러정보 중복확인")
	@RequestMapping(value = "dealerIdCheck/{userId}", method = RequestMethod.GET)
	public ResponseEntity<String> dealerIdCheck(@PathVariable("userId") String userId)  throws Exception {
		log.info("대리점정보수정");
		if(bService.dealerIdCheck(userId) > 0) {
			return new ResponseEntity<>("false", HttpStatus.OK);
		}
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
	
}
