package org.bizpay.agency.mapper;

import org.bizpay.agency.domain.param.AgencyParam;
import org.bizpay.agency.domain.param.NoticeParam;
import org.bizpay.agency.domain.param.RevenueParam;
import org.bizpay.agency.domain.param.TransactionParam;
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

    // 가맹점 매출 수익 지사/대리점 같이 사용함
    public Integer getDistributorRevenue1(RevenueParam param) throws SqlErrorException;

    // 산하 가맹점 매출 수익 지사/대리점 같이 사용함
    public Integer getDistributorRevenue2(RevenueParam param) throws SqlErrorException;
    // 추천지사 수익
    public Integer recommendRevenue(RevenueParam param) throws SqlErrorException;
    // 거래내역
    public List<TransactionInfo> getTransactionList(TransactionParam param) throws SqlErrorException;
    // 거래내역 전체 갯수
    public int getTransactionCount(TransactionParam param) throws SqlErrorException;

    public List<AgencyList> selectAgencyList(AgencyParam param) throws SqlErrorException;
    // eo
    public SettlementInfo settlementInfo(HashMap<String, String> map) throws SqlErrorException;
    public List<Notice> selectNoticeList(NoticeParam param) throws SqlErrorException;
    // 공지사항 전체 크기
    public int selectNoticeTotalCount(NoticeParam param) throws SqlErrorException;

    // 가맹비수익 목록 : 지사 대리점 공통사용
    public List<JoinAmtList> getJoinAmtList(RevenueParam param) throws SqlErrorException;
    // 가맹비수익 전체갯수
    public int getJoinAmtCount(RevenueParam param) throws SqlErrorException;
    // 가맹비 수익 :  지사 대리점 공통사용
    public int getJoinAmtSum(RevenueParam param) throws SqlErrorException;
    // 가맹점 매출수익 목록
    public List<MerchantSalesList> selectMerchantSales(RevenueParam param) throws SqlErrorException;
    // 가맹점 매출수익 목록
    public int selectMerchantSalesSum(RevenueParam param) throws SqlErrorException;
    // 가맹비 매출수익 전체갯수
    public int selectMerchantSalesCount(RevenueParam param) throws SqlErrorException;
    // 모집대리점 매출수익목록
    public List<RecruitingAgencyList> selectRecruitingAgencyList(RevenueParam param) throws SqlErrorException;
    // 모집대리점 매출전체갯수
    public int selectRecruitingAgencyCount(RevenueParam param) throws SqlErrorException;
    // 모집대리점 매출 합계
    public int selectRecruitingAgencySum(RevenueParam param) throws SqlErrorException;
    // 지사의 가맹비수익 목록
    public List<JoinAmtList> selectJoinAmtUpList(RevenueParam param) throws SqlErrorException;
    // 지사의 가맹비수익 합
    public int selectJoinAmtUpSum (RevenueParam param) throws SqlErrorException;
    // 지사의 가맹비수익 전체갯수
    public int selectJoinAmtUpCount (RevenueParam param) throws SqlErrorException;
}
