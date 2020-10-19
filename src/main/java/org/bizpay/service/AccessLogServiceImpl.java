package org.bizpay.service;

import java.util.List;

import org.bizpay.domain.AccessLog;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Log
@Service
public class AccessLogServiceImpl implements AccessLogService {

	@Override
	public void register(AccessLog accessLog) throws Exception {
		// 인터셉터를 이용한 접속 관련정보 디비나 파일로 남긴다. -- 기능개발해야함
		log.info("접속로그 상황 :"+ accessLog.toString());
	}

	@Override
	public List<AccessLog> list() throws Exception {
		log.info("접속로그 디비로 저장할경우 디비에서 정보 가져온다 ");
		return null;
	}

}
