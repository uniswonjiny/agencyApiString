package org.bizpay.service;

import java.util.List;

import org.bizpay.common.domain.GuarantParam;
import org.bizpay.common.domain.TaxIssueParam;
import org.bizpay.common.domain.TaxReportParam;
import org.bizpay.common.util.CertUtil;
import org.bizpay.domain.Guarant;
import org.bizpay.domain.TaxIssue;
import org.bizpay.domain.TaxReport;
import org.bizpay.mapper.TaxMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class TaxServiceImpl implements TaxService {
	@Autowired
	TaxMapper tMapper;
	@Autowired
	CertUtil util;
	
	@Override
	public List<TaxIssue> taxIssueList(TaxIssueParam param) throws Exception {
		List<TaxIssue> list = tMapper.taxIssueList(param);
		for (TaxIssue dto : list) {
			dto.setAdres(  util.decrypt(dto.getAdres() ) );
			dto.setMberJumi( util.decrypt(  dto.getMberJumi() ) );
			dto.setEmail( util.decrypt(  dto.getEmail() ) );
		}
		return list;
	}

	@Override
	public List<TaxReport> taxReportList(TaxReportParam param) throws Exception {
		
		
		List<TaxReport> list = tMapper.taxReportList(param);
		for (TaxReport dto : list) {
			dto.setMberJumi( util.decrypt(dto.getMberJumi()));
			dto.setMberMobile( util.decrypt(dto.getMberMobile()));
		}
		return list;
	}

	@Override
	public List<Guarant> guarantList(GuarantParam param) throws Exception {
		List<Guarant> list = tMapper.guarantList(param);
		for (Guarant dto : list) {
			dto.setMberMobile( util.decrypt( dto.getMberMobile() ) );
		}
		return list;
	}

}
