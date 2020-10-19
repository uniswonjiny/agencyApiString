package org.bizpay.service;

import java.util.List;

import org.bizpay.domain.AccessLog;

public interface AccessLogService {
	public void register(AccessLog accessLog) throws Exception;

	public List<AccessLog> list() throws Exception;
}
