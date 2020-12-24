package org.bizpay.service;

import java.util.List;

import org.bizpay.common.domain.AgencySalesParam;
import org.bizpay.domain.AgencySales;

public interface AgencyService {
	// 대리점 매출정보
	public List<AgencySales> agencySalesList(AgencySalesParam param) throws Exception;
	
}
