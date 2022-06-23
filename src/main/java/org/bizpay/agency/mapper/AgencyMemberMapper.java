package org.bizpay.agency.mapper;

import org.bizpay.agency.domain.param.*;
import org.bizpay.agency.domain.result.*;
import org.bizpay.exception.SqlErrorException;

import java.util.HashMap;
import java.util.List;

public interface AgencyMemberMapper {
    public HashMap<String, String> loginMember(String userId) throws SqlErrorException;

    public List<BizCodeCount> bossBizCodeList(List<String> list) throws SqlErrorException;

    public List<BizCodeCount> recommendBizCodeList(List<String> list) throws SqlErrorException;

    // 대리점기본정보
    public AgencyBasicInfo selectDealerBasicInfo(String userId) throws SqlErrorException;

    // 대리점 기본 계좌 정보
    public AgencyBankInfo selectDealerBankInfo(String userId) throws SqlErrorException;

    // 대리점 모집정보
    public AgencyRecruitInfo getRecruitInfo(String userId) throws SqlErrorException;

    // 영업현황
    public AgencyCount getAgencyCount(HashMap<String, String> map) throws SqlErrorException;


    // 거래내역
    public List<TransactionInfo> getTransactionList(TransactionParam param) throws SqlErrorException;
    // 거래내역 전체 갯수
    public HashMap<String, Integer> getTransactionCount(TransactionParam param) throws SqlErrorException;
    // eo
    public SettlementInfo settlementInfo(HashMap<String, String> map) throws SqlErrorException;
    public List<Notice> selectNoticeList(NoticeParam param) throws SqlErrorException;
    // 공지사항 전체 크기
    public int selectNoticeTotalCount(NoticeParam param) throws SqlErrorException;

    // 가맹비수익 목록 : 지사 대리점 공통사용
    public List<JoinAmtList> getJoinAmtList(RevenueParam param) throws SqlErrorException;
    // 모집대리점 매출수익목록
    public List<RecruitingAgencyList> selectRecruitingAgencyList(RevenueParam param) throws SqlErrorException;
    // 모집대리점 매출전체갯수
    public int selectRecruitingAgencyCount(RevenueParam param) throws SqlErrorException;
    // 모집대리점 매출 합계
    public int selectRecruitingAgencySum(RevenueParam param) throws SqlErrorException;

    // 대리점관리 목록
    public List<AgencyManagementList> selectAgencyAgencyManagementList(AgencyManageParam param) throws SqlErrorException;
    // 대리점관리 전체갯수
    public int selectAgencyAgencyManagementCount(AgencyManageParam param) throws SqlErrorException;
    // 가맹점 관리 목록
    public List<MerchantManagementList> selectMerchantManagementList(AgencyParam param) throws SqlErrorException;
    // 가맹점 관리 전체 숫자
    public int selectMerchantManagementCount(AgencyParam param) throws SqlErrorException;
    // 대리점 등록요청
    public List<ReqAgency> selectRegAgencyList(ReqAgencyParam param) throws SqlErrorException;
    // 대리점 등록요청 목록
    public int insertRegAgency(ReqAgencyParam param) throws SqlErrorException;
    // 대리점 목록 전체 갯수
    public int selectRegAgencyCount(ReqAgencyParam param) throws SqlErrorException;

/* 원래는 수익관련 부분 공통 sql 작업 하려 했으나 시간관계상 공통 sql 은 못함 - 그래서 중복 sql  이 많음*/
    /* 공통처리할부분 1. 가맹점 가져오기 mber */
    // 가맹점 매출 목록
    public List<MerchantSalesList> merchantIncomeList(RevenueParam param) throws SqlErrorException;
    // 가맹점 매출 목록 전체 갯수
    public int merchantIncomeCount(RevenueParam param) throws SqlErrorException;
    // 가맹점 매출수익 합
    public Integer merchantIncomeSum(RevenueParam param) throws SqlErrorException;

    // 대리점 가맹점 리스트
    public List<MerchantManagementList> selectFranchiseManagementList(AgencyParam param) throws SqlErrorException;
    // 대리점 가맹점 리스트 전체숫자
    public int selectFranchiseManagementCount(AgencyParam param) throws SqlErrorException;
    // 지사* 소속 대리점 매출수익 목록
    public List<RecruitingAgencyList> affiliatedAgency(RevenueParam param) throws SqlErrorException;
    // 지사* 소속 대리점 매출수익 합
    public int affiliatedAgencySum(RevenueParam param) throws SqlErrorException;
    // 지사* 소속 대리점 매출수익 전채갯수
    public int affiliatedAgencyCount(RevenueParam param) throws SqlErrorException;
    // 지사*의 가맹비수익 목록
    public List<JoinAmtList> selectJoinAmtUpList(RevenueParam param) throws SqlErrorException;
    // 지사*의 가맹비수익 합
    public Integer selectJoinAmtUpSum (RevenueParam param) throws SqlErrorException;
    // 지사*의 가맹비수익 전체갯수
    public int selectJoinAmtUpCount (RevenueParam param) throws SqlErrorException;
    // 대리점* 모집인 가맹비 목록
    public List<JoinAmtList> agencyMemberShipFeeList(RevenueParam param) throws SqlErrorException;
    // 대리점* 모집인 가맹비합계금액
    public int agencyMemberShipFeeSum(RevenueParam param) throws SqlErrorException;
    // 대리점* 모집인 가맹비 목록 전체숫자
    public int agencyMemberShipFeeCount(RevenueParam param) throws SqlErrorException;
    // 모집대리점 매출 목록
    public List<MerchantSalesList> recruitmentAgencySalesList(RevenueParam param) throws SqlErrorException;
    // 모집대리점 매출합
    public int recruitmentAgencySalesSum(RevenueParam param) throws SqlErrorException;
    // 모집대리점 매출 전체갯수
    public int recruitmentAgencySalesCount(RevenueParam param) throws SqlErrorException;
    // 지사* 소속 대리점 매출 목록
    public List<AffiliateList> affiliatedAgencySalesList(RevenueParam param) throws SqlErrorException;
    // 지사* 소속 대리점 매출 합
    public int affiliatedAgencySalesSum(RevenueParam param) throws SqlErrorException;
    // 지사* 소속 대리점 매출 전체 갯수
    public int affiliatedAgencySalesCount(RevenueParam param) throws SqlErrorException;
}
