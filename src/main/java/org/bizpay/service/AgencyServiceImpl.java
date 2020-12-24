package org.bizpay.service;

import java.util.List;

import org.bizpay.common.domain.AgencySalesParam;
import org.bizpay.domain.AgencySales;
import org.bizpay.mapper.AgencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
@Log
@Service
public class AgencyServiceImpl implements AgencyService {
	@Autowired
	AgencyMapper mapper;
	
	@Override
	public List<AgencySales> agencySalesList(AgencySalesParam param) throws Exception {
		log.info("대리점매출조회");
		
		return mapper.summaryInfo(param);
	}

}
