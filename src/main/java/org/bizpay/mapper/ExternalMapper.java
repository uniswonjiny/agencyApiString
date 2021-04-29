package org.bizpay.mapper;

import org.bizpay.common.domain.ExternalOrderInputParam;

public interface ExternalMapper {
	public int insertExOrder(ExternalOrderInputParam param) throws Exception;
}
