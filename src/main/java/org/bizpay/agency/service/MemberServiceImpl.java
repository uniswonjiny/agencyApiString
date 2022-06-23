package org.bizpay.agency.service;

import lombok.extern.java.Log;
import org.bizpay.agency.domain.param.*;
import org.bizpay.agency.domain.result.*;
import org.bizpay.agency.mapper.AgencyMemberMapper;
import org.bizpay.common.util.CertUtil;
import org.bizpay.exception.AuthErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Log
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    AgencyMemberMapper agencyMemberMapper;

    @Autowired
    CertUtil cert;

    @Override
    public HashMap<String, String> login(String userId, String passWord) throws Exception {
        log.info("로그인 확인 : " + userId);
        // 기본적으로 로그인 가능한 사용자인가 확인
        HashMap<String, String> loginMap = agencyMemberMapper.loginMember(userId);
        // 지사 영업 현황 숫자
        if (loginMap == null) throw new AuthErrorException("대리점 지사정보 없습니다.");
        if (loginMap.get("USE_AT").toString().equals("N")) throw new AuthErrorException("no");
        String confirmPassWord = cert.encrypt(passWord);
        if (!loginMap.get("PASSWORD").toString().equals(confirmPassWord)) throw new AuthErrorException("password");
        loginMap.replace("PASSWORD", ""); // 패스워드 정보 제거
        return loginMap;
    }

    @Override
    public AgencyBasicInfo dealerBasicInfo(String userId) throws Exception {
        log.info("사용자의 정보요청 ID : " + userId);
        AgencyBasicInfo agencyBasicInfo = agencyMemberMapper.selectDealerBasicInfo(userId);
        // 복호화 진행
        agencyBasicInfo.setBizTelno(cert.decrypt(agencyBasicInfo.getBizTelno()));
        agencyBasicInfo.setMTelno(cert.decrypt(agencyBasicInfo.getMTelno()));
        agencyBasicInfo.setEmail(cert.decrypt(agencyBasicInfo.getEmail()));
        return agencyBasicInfo;
    }

    @Override
    public AgencyBankInfo agencyBankInfo(String userId) throws Exception {
        log.info("대리점의 계좌정보 ID : " + userId);
        AgencyBankInfo agencyBankInfo = agencyMemberMapper.selectDealerBankInfo(userId);
        // 복호화
        agencyBankInfo.setBankSerial(cert.decrypt(agencyBankInfo.getBankSerial()));
        return agencyBankInfo;
    }

    @Override
    public AgencyRecruitInfo agencyRecruitInfo(String userId) throws Exception {
        log.info("대리점의 모집정보 ID : " + userId);
        return agencyMemberMapper.getRecruitInfo(userId);
    }

    @Override
    public AgencyCount AgencyCount(String userId, String dealerKind) throws Exception {
        log.info("영업현황");
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("dealerKind", dealerKind);
        return agencyMemberMapper.getAgencyCount(map);
    }

    @Override
    public HashMap<String, Object> transactionList(TransactionParam param) throws Exception {
        log.info(" 거래내역 조회 ");
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Integer> map2 = agencyMemberMapper.getTransactionCount(param);
        List<TransactionInfo> list = agencyMemberMapper.getTransactionList(param);
        for (TransactionInfo info : list ) {
            info.setCardNo(cert.decrypt( info.getCardNo()));
        }

        map.put("data" , list);
        map.put("count",map2.get("COUNT"));
        map.put("amount",map2.get("AMOUNT"));
        //map.putAll(agencyMemberMapper.getTransactionCount(param));
        return map;
    }

    @Override
    public HashMap<String, Object> agencyList(AgencyManageParam param) throws Exception {
        log.info(" 대리점관리 대리잠 지사 목록  조회 ");
        HashMap<String, Object> map = new HashMap<>();

        List<AgencyManagementList> list = agencyMemberMapper.selectAgencyAgencyManagementList(param);

        for (AgencyManagementList info : list ) {
            info.setEmail(cert.decrypt(info.getEmail()));
            info.setBankSerial(cert.decrypt(info.getBankSerial()));
            info.setMTelno(cert.decrypt(info.getMTelno()));
            info.setBplc(cert.decrypt(info.getBplc()));
        }

        map.put("count" , agencyMemberMapper.selectAgencyAgencyManagementCount(param));
        map.put("list" , list);

        return map;
    }

    @Override
    public SettlementInfo settlementInfo(String dealerId, String cclDt) throws Exception {
        log.info(" 대리점 정산서 ");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("dealerId", dealerId);
        map.put("cclDt", cclDt);
        return agencyMemberMapper.settlementInfo(map);
    }

    @Override
    public HashMap<String, Object> selectNoticeList(NoticeParam param) throws Exception {
        log.info(" 공지사항 ");
        HashMap<String, Object> map = new HashMap<String, Object>();
        int totalCount = agencyMemberMapper.selectNoticeTotalCount(param);
        List<Notice> list =agencyMemberMapper.selectNoticeList(param);
        map.put("count" , totalCount);
        map.put("data" , list);
        return map;
    }

    @Override
    public HashMap<String, Object> inComeInfo(RevenueParam param) throws Exception {
        log.info(" 수익현황 전체합부분");
        HashMap<String, Object> map = new HashMap<String, Object>();
        // 대리점인 경우
        if(param.getDealerKind() == 34){
            map.put("agencyMemberShipFeeSum", agencyMemberMapper.agencyMemberShipFeeList(param) ); // 가맹비
            map.put("recruitmentAgencySalesSum",agencyMemberMapper.merchantIncomeSum(param) ); // 모집대리점 매출수익
            map.put("selectRecruitingAgencySum", agencyMemberMapper.selectRecruitingAgencySum(param)); // 모집대리점 매출수익
        }
        // 지사인 경우
        if(param.getDealerKind() == 33){
            Integer agencyMemberShipFeeSum = agencyMemberMapper.selectJoinAmtUpSum(param);
            Integer recruitmentAgencySalesSum = agencyMemberMapper.merchantIncomeSum(param);

            map.put("agencyMemberShipFeeSum",agencyMemberShipFeeSum == null ? 0 : agencyMemberShipFeeSum  ) ; // 가맹비
            map.put("recruitmentAgencySalesSum", recruitmentAgencySalesSum == null ? 0 : recruitmentAgencySalesSum ); // 소속대리점 매출수익

        }
        return map;
    }

    @Override
    public HashMap<String, Object> merchantManagementList(AgencyParam param) throws Exception {
        log.info("가맹점 목록정보조회");
        HashMap<String, Object> map = new HashMap<String, Object>();

        List<MerchantManagementList> list = agencyMemberMapper.selectMerchantManagementList(param);
        for (MerchantManagementList info : list ) {
            info.setEmail(cert.decrypt(info.getEmail()));
            info.setAccountNo(cert.decrypt(info.getAccountNo()));
            info.setAdres(cert.decrypt(info.getAdres()));
            info.setMberMobile(cert.decrypt(info.getMberMobile()));
        }
        map.put("count", agencyMemberMapper.selectMerchantManagementCount(param));
        map.put("data", list);
        return map;
    }
    @Override
    public int insertRegAgency(ReqAgencyParam param) throws Exception {
        if (param.getEmail()!= null && !param.getEmail().equals("")) {
            param.setEmail(cert.encrypt(param.getEmail()));
        }
        if (param.getMobileNumber() != null && !param.getMobileNumber().equals("")) {
            param.setMobileNumber(cert.encrypt(param.getMobileNumber()));
        }
        return agencyMemberMapper.insertRegAgency(param);
    }

    @Override
    public HashMap<String, Object> selectRegAgencyList(ReqAgencyParam param) throws Exception {
        log.info("대리점등록목록 조회");
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<ReqAgency> list = agencyMemberMapper.selectRegAgencyList(param);
        if(list.size() > 0){
            for (ReqAgency agency : list) {
                agency.setEmail( cert.decrypt( agency.getEmail()));
                agency.setMobileNumber(cert.decrypt(agency.getMobileNumber()));
            }
        }
        map.put("list", list);
        map.put("count", agencyMemberMapper.selectRegAgencyCount(param));
        return map;
    }

}
