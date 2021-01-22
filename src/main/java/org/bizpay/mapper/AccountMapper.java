package org.bizpay.mapper;

import java.util.List;

import org.bizpay.common.domain.AccountExcelParam;
import org.bizpay.common.domain.AccountListParam;
import org.bizpay.domain.AccountExcel;
import org.bizpay.domain.AccountInOut;

// 계좌관련부분정리
public interface AccountMapper {
	// 입출력내역관리
	public List<AccountInOut> accountInOutList(AccountListParam param) throws Exception;
	// 정산용엑셀
	public List<AccountExcel> accountExcelList(AccountExcelParam param) throws Exception;
}
