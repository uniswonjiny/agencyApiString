package org.bizpay.service;

import java.util.List;

import org.bizpay.common.domain.AccountExcelParam;
import org.bizpay.common.domain.AccountListParam;
import org.bizpay.common.util.CertUtil;
import org.bizpay.domain.AccountExcel;
import org.bizpay.domain.AccountInOut;
import org.bizpay.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
@Log
@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	CertUtil cUtil;
	
	@Autowired
	AccountMapper aMaper;

	@Override
	public List<AccountInOut> AccountInList(AccountListParam param) throws Exception {
		log.info("입출력내역 조회");
		
		List<AccountInOut> list = aMaper.accountInOutList(param);
		for (AccountInOut dto : list) {
			dto.setAccount( cUtil.decrypt(dto.getAccount()) );
		}
		return list;
	}

	@Override
	public List<AccountExcel> accountExcelList(AccountExcelParam param) throws Exception {
		List<AccountExcel> list = aMaper.accountExcelList(param);
		
		return list;
	}

}
