package org.bizpay.mapper;

import java.util.List;

import org.bizpay.common.domain.GuarantParam;
import org.bizpay.common.domain.TaxIssueParam;
import org.bizpay.common.domain.TaxReportParam;
import org.bizpay.domain.Guarant;
import org.bizpay.domain.TaxIssue;
import org.bizpay.domain.TaxReport;

public interface TaxMapper {
	// 세금계산서발급목록
	public List<TaxIssue> taxIssueList(TaxIssueParam param) throws Exception;
	// 세무서신고 목록
	public List<TaxReport> taxReportList(TaxReportParam param) throws Exception;
	// 보증료조회
	public List<Guarant> guarantList(GuarantParam param) throws Exception;
}
