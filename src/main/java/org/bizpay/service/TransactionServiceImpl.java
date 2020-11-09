package org.bizpay.service;

import java.util.List;

import org.bizpay.common.domain.InqireDelingParam;
import org.bizpay.domain.TransByDate;
import org.bizpay.mapper.InqireDelngMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
@Log
@Service
public class TransactionServiceImpl implements TransactionService {
	@Autowired
	InqireDelngMapper delngMapper; // 거래내역
	
	@Override
	public  List<TransByDate> TransByDateList(InqireDelingParam param) throws Exception {
		log.info("일자별 거래내역조회");
		return delngMapper.transByDate(param);
	}
}