package org.bizpay.mapper;

import java.util.List;

import org.bizpay.common.domain.AgencySalesParam;
import org.bizpay.domain.AgencySales;

//거래내역
public interface AgencyMapper {	
	// 거래조회 취소내역
	public List<AgencySales> summaryInfo(AgencySalesParam param) throws Exception;
}
