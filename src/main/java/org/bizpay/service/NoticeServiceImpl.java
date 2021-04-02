package org.bizpay.service;

import java.sql.SQLException;
import java.util.List;

import org.bizpay.domain.Notice;
import org.bizpay.domain.PgFee;
import org.bizpay.exception.SqlErrorException;
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
	@Transactional(rollbackFor = SqlErrorException.class)
	public int deleteNotice(List<Integer> list) throws SqlErrorException {
		return nMapper.deleteNotice(list);
	}

	@Override
	@Transactional(rollbackFor = SqlErrorException.class)
	public int saveNotice(Notice notice) throws SqlErrorException {
		if(nMapper.saveNotice(notice) <1) {			
			throw new SqlErrorException("공지사항 저장중 문제가 발생했습니다.");
		}
		return 1;
	}

	@Override
	@Transactional
	public int updateNotice(Notice notice) throws SqlErrorException {
		return nMapper.updateNotice(notice);
	}

	@Override
	@Transactional(rollbackFor = SqlErrorException.class)
	public int savePgFee(PgFee pgFee) throws SqlErrorException {
		return nMapper.savePgFee(pgFee);
	}

	@Override
	@Transactional(rollbackFor = SqlErrorException.class)
	public int updatePgFee(PgFee pgFee) throws SqlErrorException {
		return nMapper.updatePgFee(pgFee);
	}

	@Override
	@Transactional(rollbackFor = SqlErrorException.class)
	public int deletePgFee(List<Integer> list) throws SqlErrorException {
		return nMapper.deletePgFee(list);
	}
}
