package org.bizpay.mapper;

import java.util.List;

import org.bizpay.domain.Notice;
import org.bizpay.domain.PgFee;

public interface NoticeMapper {
	public List<Notice> noticeList() throws Exception;
	public List<PgFee> pgFeeList() throws Exception;
}
