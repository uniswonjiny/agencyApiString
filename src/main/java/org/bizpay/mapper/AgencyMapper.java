package org.bizpay.mapper;

import java.util.HashMap;
import java.util.List;

import org.bizpay.common.domain.AgencySalesParam;
import org.bizpay.common.domain.DelngCancelParam;
import org.bizpay.common.domain.SellerParam;
import org.bizpay.domain.AgencySales;
import org.bizpay.domain.AgencySales2;
import org.bizpay.domain.AgencySales3;
import org.bizpay.domain.SalesAdjustment;
import org.bizpay.domain.SellerSummary;

//대리점별매출 -- 복잡한 중간 활용값가져오기 부분때문에 일단 대리점 매출관련은 모두 이곳에 집중및 그대로 실행한다 중복되도 어쩔수 없음. 너무 햇갈림 !!!!!! 나중에 매출 전면 개편!!!!! 
public interface AgencyMapper {	
	// 대리점별 매출수익
	public List<AgencySales> summaryInfo(AgencySalesParam param) throws Exception;
	// 추천수수료 수익
	public List<AgencySales2> summaryInfo2(AgencySalesParam param) throws Exception;
	// 가맹비 수익1
	public List<String> summaryInfo3in0(AgencySalesParam param) throws Exception;
	//가맹비 수익2
	public List<AgencySales3> summaryInfo3in1(AgencySalesParam param) throws Exception;
	// 가맹비 수익3
	public List<AgencySales3> summaryInfo3in2(AgencySalesParam param) throws Exception;
	// 대리점 매출
	public List<SellerSummary> sellerSummaryList(SellerParam param) throws Exception;
	// 매출조정 내역조회
	public List<SalesAdjustment> salesAdjustmentList(SellerParam param) throws Exception;
	// 매출내역 취소
	public int delngCancel(DelngCancelParam param) throws Exception;
	// 매출정보확인
	public DelngCancelParam delngConfirm (DelngCancelParam param)throws Exception;
	// 매출카드정보확인
	public DelngCancelParam delngCardConfirm (DelngCancelParam param) throws Exception;
	// 매출종류 확인
	public String getPayType(int memberCode) throws Exception; 
	// 매출 확정날짜확인
	public String getConfmDt(DelngCancelParam param) throws Exception;
	// -- 매출조정에서 사용하는 부분
	// 중복검사
	public int newtbDelng(HashMap<String, Object> map) throws Exception;
	// 최대 사용자 코드 
	public int selectMaxMberCodeSn(String mberCode) throws Exception;
	// 사용자 정보 MberBasis 에 있는 정보
	public HashMap<String, Object> tbMberBasis(String indutyId) throws Exception;
	// 최대MAX(rcipt_no)+1 FROM delng 값 구하기
	public int selectMaxRciptNo(HashMap<String, Object> map) throws Exception;
	// 영수증번호존재 rcipt_no 존재 확인
	public int selectRciptNoCount(HashMap<String, Object> map) throws Exception;
	// 매출정보 입력1
	public int insertDelng(HashMap<String, Object> map) throws Exception;
	// 매출정보 입력2
	public int insertDelngCredt(HashMap<String, Object> map) throws Exception;
	// 매출정보 입력3
	public int pushAtmInfo(HashMap<String, Object> map) throws Exception;
	// 매출정보 입력4
	public int insertDelngAdi(HashMap<String, Object> map) throws Exception;
	// 매출정보 입력 5
	public int insertDelngCash(HashMap<String, Object> map) throws Exception;
	// 잔액 구하기
	public double selectBalance(String mbrCode) throws Exception;
	// 입출금 번호
	public double selectInoutNo(String mbrCode) throws Exception;
	// 현금결제 중복확인
	public int selectCashCount(HashMap<String, Object> map) throws Exception;
	
}
