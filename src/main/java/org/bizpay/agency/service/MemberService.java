package org.bizpay.agency.service;

import org.bizpay.agency.domain.param.*;
import org.bizpay.agency.domain.result.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

public interface MemberService {
    // 로그인 // 로그인 인증없이 용가능한 메뉴 이기능 만들어야함
    public HashMap<String, String> login(String userId, String passWord) throws Exception;
    // 대리점 기본 정보 // 로그인이나 인증키 여부 확인 하는 기능 만들어야 한다.
    public AgencyBasicInfo dealerBasicInfo(String userId) throws Exception;
    // 대리점 계좌 정보
    public AgencyBankInfo agencyBankInfo(String userId) throws Exception;
    // 대리점 모집정보
    public AgencyRecruitInfo agencyRecruitInfo(String userId) throws Exception;
    // 영업현황
    public AgencyCount AgencyCount(String userId, String dealerKind) throws Exception;
    // 거래내역
    public HashMap<String, Object> transactionList(TransactionParam param) throws Exception;
    // 대리점 지사 록록
    public HashMap<String, Object> agencyList(AgencyManageParam param) throws Exception;
    // 대리점 정산서
    public SettlementInfo settlementInfo(String dealerId, String cclDt) throws Exception;
    // 공지사항
    public HashMap<String, Object> selectNoticeList(NoticeParam param) throws Exception;
    // 수익현황전체합부분
    public HashMap<String, Object> inComeInfo(RevenueParam param) throws Exception;
    // 가매점정보
    public HashMap<String, Object> merchantManagementList(AgencyParam param) throws Exception;
    // 대리점등록요청
    public int insertRegAgency(ReqAgencyParam param) throws Exception;
    // 대리점 등록 요청 사항목록
    public HashMap<String, Object> selectRegAgencyList(ReqAgencyParam param) throws Exception;
}
