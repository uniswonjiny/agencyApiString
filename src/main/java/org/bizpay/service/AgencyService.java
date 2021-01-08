package org.bizpay.service;

import java.util.List;

import org.bizpay.common.domain.AgencySalesParam;
import org.bizpay.common.domain.SellerParam;
import org.bizpay.domain.AgencySales;
import org.bizpay.domain.AgencySales2;
import org.bizpay.domain.AgencySales3;
import org.bizpay.domain.SalesAdjustment;
import org.bizpay.domain.SellerSummary;

public interface AgencyService {
	// 대리점 매출수익
	public List<AgencySales> agencySalesList(AgencySalesParam param) throws Exception;
	// 추천 수수료수익
	public List<AgencySales2> agencySalesList2(AgencySalesParam param) throws Exception;
	// 가맹비 수익
	public List<AgencySales3> agencySalesList3(AgencySalesParam param) throws Exception;
	// 대리점 매출
	public List<SellerSummary> sellerSummaryList(SellerParam param) throws Exception;
	// 매출조정 내역조회
	public List<SalesAdjustment> salesAdjustment(SellerParam param) throws Exception;
}
