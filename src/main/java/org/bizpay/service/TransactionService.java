package org.bizpay.service;

import java.util.List;

import org.bizpay.common.domain.InqireDelingParam;
import org.bizpay.domain.InqireDelng;
import org.bizpay.domain.TransByDate;

public interface TransactionService {
	// 달력형태 매일 매출 내역 시작일부터 종료일까지
	public List<TransByDate> TransByDateList(InqireDelingParam param) throws Exception;
	
	// 검색기간별 거래 / 취소 조회 내역
	public List<InqireDelng> transSearchList(InqireDelingParam param) throws Exception;
}
