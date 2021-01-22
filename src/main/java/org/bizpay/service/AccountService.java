package org.bizpay.service;

import java.util.List;

import org.bizpay.common.domain.AccountExcelParam;
import org.bizpay.common.domain.AccountListParam;
import org.bizpay.domain.AccountExcel;
import org.bizpay.domain.AccountInOut;

public interface AccountService {
	// 입출력내역목록
	public List<AccountInOut> AccountInList(AccountListParam param)  throws Exception;
	// 정산용엑셀
	public List<AccountExcel> accountExcelList(AccountExcelParam param) throws Exception;
}
