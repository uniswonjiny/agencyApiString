package org.bizpay.service;

import java.util.List;

import org.bizpay.domain.Notice;
import org.bizpay.domain.PgFee;
import org.bizpay.exception.SqlErrorException;

public interface NoticeService {
	public List<Notice> noticeList() throws Exception;
	public List<PgFee> pgFeeList() throws Exception;
	public int deleteNotice(List<Integer> list) throws SqlErrorException;
	public int saveNotice(Notice notice) throws SqlErrorException;
	public int updateNotice(Notice notice) throws SqlErrorException;
	public int savePgFee(PgFee pgFee) throws SqlErrorException;
	public int updatePgFee(PgFee pgFee) throws SqlErrorException;
	public int deletePgFee(List<Integer> list) throws SqlErrorException;
}
