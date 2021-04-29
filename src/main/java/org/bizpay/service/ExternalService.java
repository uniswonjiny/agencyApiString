package org.bizpay.service;

import org.bizpay.common.domain.ExternalOrderInputParam;

public interface ExternalService {
	public int insertExOrder(ExternalOrderInputParam param) throws Exception;
}
