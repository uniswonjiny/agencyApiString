package org.bizpay.service;

import java.util.List;

import org.bizpay.domain.Notice;
import org.bizpay.domain.PgFee;
import org.bizpay.mapper.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class NoticeServiceImpl implements NoticeService {
	@Autowired
	NoticeMapper nMapper;
	
	@Override
	public List<Notice> noticeList() throws Exception {
		return nMapper.noticeList();
	}

	@Override
	public List<PgFee> pgFeeList() throws Exception {
		return nMapper.pgFeeList();
	}

}
