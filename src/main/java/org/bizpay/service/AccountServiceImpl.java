package org.bizpay.service;

import java.util.HashMap;
import java.util.List;

import org.bizpay.common.domain.AccountExcelParam;
import org.bizpay.common.domain.AccountListParam;
import org.bizpay.common.domain.AccountTransParam;
import org.bizpay.common.domain.BankAcntTransParam;
import org.bizpay.common.util.CertUtil;
import org.bizpay.domain.AccountExcel;
import org.bizpay.domain.AccountInOut;
import org.bizpay.domain.AccountTrans;
import org.bizpay.domain.BankAcntTrans;
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

	@Override
	public List<AccountTrans> transferList(AccountTransParam param) throws Exception {
		 List<AccountTrans> list = aMaper.transferList(param);
		 for (AccountTrans dto : list) {
			dto.setAccountNO( cUtil.decrypt(dto.getAccountNO()) );
		}
		return list;
	}

	@Override
	public List<BankAcntTrans> bankAcntTransList(BankAcntTransParam param) throws Exception {
		List<BankAcntTrans> list = aMaper.bankAcntTrans(param);
		for (BankAcntTrans dto : list) {
			// 관리자인지 검사 필요 -- 세션기능 인터셉터 기능 강화시 추가할 예정 지금은 급함 - 계좌번호
			dto.setAccountNo( cUtil.decrypt(dto.getAccountNo()));
		}
		return list;
	}

	@Override
	public int inOutSetting(HashMap<String, Object> map) throws Exception {
		return aMaper.inOutSetting(map);
	}

	@Override
	public HashMap<String, Object> inOutSettingInfo() throws Exception {
		return aMaper.inOutSettingInfo();
	}

}
