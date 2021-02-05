package org.bizpay.mapper;

import java.util.List;

import org.bizpay.domain.Notice;
import org.bizpay.domain.PgFee;

public interface NoticeMapper {
	public List<Notice> noticeList() throws Exception;
	public List<PgFee> pgFeeList() throws Exception;
	public int deleteNotice(List<Integer> list) throws Exception;
	public int saveNotice(Notice notice) throws Exception;
	public int updateNotice(Notice notice) throws Exception;
	public int savePgFee(PgFee pgFee) throws Exception;
	public int updatePgFee(PgFee pgFee) throws Exception;
	public int deletePgFee(List<Integer> list) throws Exception;
}
