package org.bizpay.service;

import java.util.List;

import org.bizpay.common.domain.InqireDelingParam;
import org.bizpay.common.util.CertUtil;
import org.bizpay.domain.InqireDelng;
import org.bizpay.domain.TransByDate;
import org.bizpay.mapper.InqireDelngMapper;
import org.bizpay.mapper.TransMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
@Log
@Service
public class TransactionServiceImpl implements TransactionService {
	@Autowired
	InqireDelngMapper delngMapper; // 거래내역
	@Autowired
	TransMapper transMapper;
	
	@Autowired
	CertUtil util;
	
	@Override
	public  List<TransByDate> TransByDateList(InqireDelingParam param) throws Exception {
		log.info("일자별 거래내역조회");
		return delngMapper.transByDate(param);
	}

	@Override
	public List<InqireDelng> transSearchList(InqireDelingParam param) throws Exception {
		log.info("검색기간 거래목록내역조회");
		List<InqireDelng> list = transMapper.transInfo(param);
		for (InqireDelng dto : list) {
			dto.setAdres( util.decrypt(dto.getAdres()) );
			dto.setCardNo( util.decrypt(dto.getCardNo()) );
			dto.setMberMobile( util.decrypt(dto.getMberMobile()) );
			dto.setMberPhone( util.decrypt(dto.getMberPhone()) );
			dto.setBizTelno( util.decrypt(dto.getBizTelno()) );
		}
		return list;
	}
	
	
}