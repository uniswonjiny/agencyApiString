package org.bizpay.service;

import java.util.List;

import org.bizpay.common.domain.InqireDelingParam;
import org.bizpay.domain.TransByDate;

public interface TransactionService {
	public List<TransByDate> TransByDateList(InqireDelingParam param) throws Exception;
}
