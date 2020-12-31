package org.bizpay.service;

import java.util.List;

import org.bizpay.common.domain.AgencySalesParam;
import org.bizpay.domain.AgencySales;
import org.bizpay.domain.AgencySales2;

public interface AgencyService {
	// 대리점 매출수익
	public List<AgencySales> agencySalesList(AgencySalesParam param) throws Exception;
	// 추천 수수료수익
	public List<AgencySales2> agencySalesList2(AgencySalesParam param) throws Exception;
}
