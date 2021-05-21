package org.bizpay.service;

import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.common.domain.PaymentReqParam;

public interface ExternalService {
	public String insertExOrder(ExternalOrderInputParam param) throws Exception;
	public ExternalOrderInputParam selectOrderInfo(long orderNo) throws Exception;
	public boolean payRequest(PaymentReqParam param)throws Exception;
	public void payCancel(ExternalOrderInputParam param)throws Exception;
}
