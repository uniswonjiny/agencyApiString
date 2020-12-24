package org.bizpay.mapper;

import java.util.List;

import org.bizpay.common.domain.InqireDelingParam;
import org.bizpay.domain.InqireDelng;
import org.bizpay.domain.InqireDelngSum;
//거래내역
import org.bizpay.domain.TransByDate;
public interface TransMapper {	
	// 거래조회 취소내역
	public List<InqireDelng> transInfo(InqireDelingParam param) throws Exception;
}
