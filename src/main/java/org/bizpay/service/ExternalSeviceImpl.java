package org.bizpay.service;

import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.mapper.ExternalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;
@Log
@Service
public class ExternalSeviceImpl implements ExternalService {
	@Autowired
	ExternalMapper exMapper;
	
	@Override
	@Transactional
	public int insertExOrder(ExternalOrderInputParam param) throws Exception {
		log.info("외부결제연동 결제정보 입력");
		return exMapper.insertExOrder(param);
	}

}
