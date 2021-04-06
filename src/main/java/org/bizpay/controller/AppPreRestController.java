package org.bizpay.controller;
// 이전버전 앱버전 호환을위한 컨트롤러
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.bizpay.service.AppPreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/bizpay")
@CrossOrigin(origins={"*"})
@Api(tags = "이전API - Command ")
public class AppPreRestController {
	@Autowired
	AppPreService apServe;
	@ApiOperation(value=" 앱통신부분" , notes = "앱통신부분 - 여러호출이 Command 구분후 서비스로 연결됨")
	@RequestMapping(value = "Command", method = RequestMethod.POST)
	public ResponseEntity<HashMap<String, Object>> Command(HttpServletRequest req) throws Exception{
		log.info("앱통신부분");
		String command = req.getParameter("command");
		HashMap<String, Object> map = new HashMap<String, Object>();
		if("BPALoginCommand".equals(command ) ) {
			map = apServe.login(req);
		}
//		"password=6759"
//		"induty_id=B0002A3749"
//		"key=ef72eb3b5895818fb0076d222b57575212b8a46f15769e058291d2213906a4db961a54a83f2dcc20ea2f3bd95ca6fe4f4d9cbaae89e338c83c64d8171535b0316338be97dd680ba0ed8dca25e9a3152b"
//		1
//		"command=BPALoginCommand"
		return new ResponseEntity<>(map, HttpStatus.OK);	
		
	}
}
