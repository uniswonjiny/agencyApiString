package org.bizpay.agency.controller;

import lombok.extern.java.Log;
import org.bizpay.agency.domain.param.*;
import org.bizpay.agency.domain.result.*;
import org.bizpay.agency.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@Log
@CrossOrigin(origins = {"*"})
@RequestMapping("/agency")
public class MemberController {
    @Autowired
    MemberService memberService;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<HashMap<String, String>> login(@RequestBody Login login) throws Exception {
        log.info("로그인");
        // 로그인 가능과 간단한 정보만 내보낸다. 추후 세션아 아니다.- 인증키는 대응 부분에서 로그인인증다룬다.
        HashMap<String, String> map = memberService.login(login.getUserId(), login.getPassWord());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/basicInfo/{userId}")
    @ResponseBody
    public ResponseEntity<AgencyMainInfo> basicInfo(@PathVariable("userId") String userId) throws Exception {
        log.info("사용자 기본정보");
        // 로그인 가능과 간단한 정보만 내보낸다. 추후 세션아 아니다.- 인증키는 대응 부분에서 로그인인증다룬다.
        AgencyMainInfo agencyMainInfo = new AgencyMainInfo();
        agencyMainInfo.setAgencyBasicInfo( memberService.dealerBasicInfo(userId) );
        agencyMainInfo.setAgencyBankInfo( memberService.agencyBankInfo(userId)  );
        agencyMainInfo.setAgencyRecruitInfo(memberService.agencyRecruitInfo(userId) );
        return new ResponseEntity<>(agencyMainInfo, HttpStatus.OK);
    }

    @GetMapping("/agencyCount/{userId}/{dealerKind}")
    @ResponseBody
    public ResponseEntity<AgencyCount> getAgencyCount2(@PathVariable("userId") String userId, @PathVariable("dealerKind") String dealerKind) throws Exception {
        log.info("영업현황");
        return new ResponseEntity<>(memberService.AgencyCount(userId, dealerKind), HttpStatus.OK);
    }

    @PostMapping("/transactionList")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> transactionList(@RequestBody TransactionParam param) throws Exception {
        log.info("거래조회");
        return new ResponseEntity<>(memberService.transactionList(param), HttpStatus.OK);
    }

    @PostMapping("/agencyList")
    @ResponseBody
    public ResponseEntity<List<AgencyList>> agencyList(@RequestBody AgencyParam param) throws Exception {
        log.info("대리점 지사 목록");
        return new ResponseEntity<>(memberService.agencyList(param), HttpStatus.OK);
    }

    @GetMapping("/settlementInfo/{dealerId}/{cclDt}")
    @ResponseBody
    public ResponseEntity<SettlementInfo> settlementInfo(@PathVariable("dealerId") String dealerId, @PathVariable("cclDt") String cclDt) throws Exception {
        log.info("대리점 정산서");
        return new ResponseEntity<>(memberService.settlementInfo(dealerId, cclDt), HttpStatus.OK);
    }

    @PostMapping("/noticeList")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> noticeList(@RequestBody NoticeParam param) throws Exception {
        log.info("공지사항 목록");
        return new ResponseEntity<>(memberService.selectNoticeList(param), HttpStatus.OK);
    }
    // 수익현황 전체 수익현황부분
    @PostMapping("/incomeInfo")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> incomeInfo(@RequestBody RevenueParam param) throws Exception {
        log.info("대리점 수익현황 전체 합계부분");
        return new ResponseEntity<>(memberService.inComeInfo(param), HttpStatus.OK);
    }
}
