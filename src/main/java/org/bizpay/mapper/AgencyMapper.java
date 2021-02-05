package org.bizpay.mapper;

import java.util.List;

import org.bizpay.common.domain.AgencySalesParam;
import org.bizpay.common.domain.DelngCancelParam;
import org.bizpay.common.domain.SellerParam;
import org.bizpay.domain.AgencySales;
import org.bizpay.domain.AgencySales2;
import org.bizpay.domain.AgencySales3;
import org.bizpay.domain.SalesAdjustment;
import org.bizpay.domain.SellerSummary;

//대리점별매출
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

}
