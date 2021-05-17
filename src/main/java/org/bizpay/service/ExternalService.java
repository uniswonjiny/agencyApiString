package org.bizpay.service;

import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.common.domain.PaymentReqParam;
import org.bizpay.domain.ExternalOrderInfo;

public interface ExternalService {
	public long insertExOrder(ExternalOrderInputParam param) throws Exception;
	public ExternalOrderInfo selectOrderInfo(long orderNo) throws Exception;
	public boolean payRequest(PaymentReqParam param)throws Exception;
}
