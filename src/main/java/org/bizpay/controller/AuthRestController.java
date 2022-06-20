package org.bizpay.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bizpay.common.domain.LoginParam;
import org.bizpay.common.util.JwtUtil;
import org.bizpay.domain.BizInfo;
import org.bizpay.domain.DealerInfo;
import org.bizpay.domain.MemberInfo;
import org.bizpay.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthRestController {

    @Autowired
    AuthService aService;

    @Autowired
    JwtUtil jwt;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginParam param) throws Exception {
        log.info("로그인시도 ID : " + param.getUserId());
        Map<String, Object> map = new HashMap<String, Object>();
        // 로그인가능 유무확인
        MemberInfo info = aService.loginConfirm(param.getUserId(), param.getPassword());
        // 사업자 정보
        BizInfo bizInfo = aService.bizInfo(info.getBizCode());
        // 딜러정보
        List<DealerInfo> dealerList = aService.dealerList(info.getBizCode());
        // 인증키 생성
        String key = jwt.getJwtKey(info.getUsid());
        map.put("member", info);
        map.put("biz", bizInfo);
        map.put("dealerList", dealerList);
        map.put("authkey", key);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping(value = "/relogin", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> reLogin(
            @RequestHeader(value = "userId") String userId,
            @RequestHeader(value = "authkey") String authkey
    ) throws Exception {
        log.info("인증키 확인후 로그인 관련 정보를 추출한다.");
        Map<String, Object> map = new HashMap<String, Object>();
        if (jwt.parsedKey(authkey, userId)) {
            // 사용자정보
            MemberInfo info = aService.memberInfo(userId);
            // 사업자정보
            BizInfo bizInfo = aService.bizInfo(info.getBizCode());
            // 딜러정보
            List<DealerInfo> dealerList = aService.dealerList(info.getBizCode());
            map.put("member", info);
            map.put("biz", bizInfo);
            map.put("dealerList", dealerList);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = "/idChk/{id}", method = RequestMethod.GET)
    public ResponseEntity<Integer> idChk(@PathVariable String id)
            throws Exception {
        return new ResponseEntity<>(aService.memberIdChk(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/biznoChk/{bizno}", method = RequestMethod.GET)
    public ResponseEntity<Integer> biznoChk(@PathVariable String bizno)
            throws Exception {
        return new ResponseEntity<>(aService.biznoChk(bizno), HttpStatus.OK);
    }
}
