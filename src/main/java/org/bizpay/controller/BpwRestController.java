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

import lombok.extern.java.Log;
@Log
@RestController
@RequestMapping("/bpw")
@CrossOrigin(origins={"*"})
public class BpwRestController {
	@Autowired
	BPWService bService;
	
	// 입출금내역
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
	
	@RequestMapping(value = "atmConfig/{gbCode}", method = RequestMethod.GET)
	public ResponseEntity<AtmConfig> atmConfigInfo(@PathVariable("gbCode") String gbCode)  throws Exception {
		log.info("출금정지 상태");
		AtmConfig ac = bService.atmConfigInfo(gbCode);
		return new ResponseEntity<>(ac, HttpStatus.OK);
	}

	@RequestMapping(value = "atmConfig", method = RequestMethod.POST)
	public ResponseEntity<String> atmConfigInfo(@RequestBody AtmConfig param)  throws Exception {
		log.info("출금정지상태변경");
		if(bService.atmConfigUpdate(param)) return new ResponseEntity<>("ok", HttpStatus.OK);
		else return new ResponseEntity<>("fail", HttpStatus.ACCEPTED);//202  요청은 응했지만 실행되지 않았다.
	}
	
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

	public ResponseEntity<DealerRegInfo> dealerbyId(@PathVariable("userId") String userId)  throws Exception {
		log.info("개별대리점 상세정보 확인");
		DealerRegInfo info = bService.dealerbyId(userId);
		return new ResponseEntity<>(info, HttpStatus.OK);
	}

	@RequestMapping(value = "dealerCreate", method = RequestMethod.POST)
	public ResponseEntity<String> dealerCreate(@RequestBody DealerRegInfo dealerInfo)  throws Exception {
		log.info("신규대리점정보 입력");
		//DealerRegInfo info = bService.dealerbyId(userId);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
	
	@RequestMapping(value = "dealerModify", method = RequestMethod.POST)
	public ResponseEntity<String> dealerUpdate(@RequestBody  DealerRegInfo dealerInfo)  throws Exception {
		log.info("대리점정보수정");
		bService.updateDealer(dealerInfo);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
	
	@RequestMapping(value = "dealerIdCheck/{userId}", method = RequestMethod.GET)
	public ResponseEntity<String> dealerIdCheck(@PathVariable("userId") String userId)  throws Exception {
		log.info("대리점정보수정");
		if(bService.dealerIdCheck(userId) > 0) {
			return new ResponseEntity<>("false", HttpStatus.OK);
		}
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
}
