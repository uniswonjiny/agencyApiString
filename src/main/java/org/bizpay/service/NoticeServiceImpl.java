package org.bizpay.service;

import java.util.List;

import org.bizpay.domain.Notice;
import org.bizpay.domain.PgFee;
import org.bizpay.mapper.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

	@Override
	@Transactional
	public int deleteNotice(List<Integer> list) throws Exception {
		return nMapper.deleteNotice(list);
	}

	@Override
	@Transactional
	public int saveNotice(Notice notice) throws Exception {
		return nMapper.saveNotice(notice);
	}

	@Override
	@Transactional
	public int updateNotice(Notice notice) throws Exception {
		return nMapper.updateNotice(notice);
	}

	@Override
	@Transactional
	public int savePgFee(PgFee pgFee) throws Exception {
		return nMapper.savePgFee(pgFee);
	}

	@Override
	@Transactional
	public int updatePgFee(PgFee pgFee) throws Exception {
		return nMapper.updatePgFee(pgFee);
	}

	@Override
	@Transactional
	public int deletePgFee(List<Integer> list) throws Exception {
		return nMapper.deletePgFee(list);
	}
}
