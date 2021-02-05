package org.bizpay.mapper;

import java.util.HashMap;
import java.util.List;

import org.bizpay.common.domain.AccountExcelParam;
import org.bizpay.common.domain.AccountListParam;
import org.bizpay.common.domain.AccountTransParam;
import org.bizpay.common.domain.BankAcntTransParam;
import org.bizpay.domain.AccountExcel;
import org.bizpay.domain.AccountInOut;
import org.bizpay.domain.AccountTrans;
import org.bizpay.domain.BankAcntTrans;

// 계좌관련부분정리
public interface AccountMapper {
	// 입출력내역관리
	public List<AccountInOut> accountInOutList(AccountListParam param) throws Exception;
	// 정산용엑셀
	public List<AccountExcel> accountExcelList(AccountExcelParam param) throws Exception;
	// 계좌이체조정 목록
	public List<AccountTrans> transferList(AccountTransParam param) throws Exception;
	// 은행계좌이체
	public List<BankAcntTrans> bankAcntTrans(BankAcntTransParam param) throws Exception;
	// 출금정지설정
	public int inOutSetting(HashMap<String, Object> map) throws Exception;
	// 출금정지상태
	public HashMap<String, Object> inOutSettingInfo() throws Exception;
	
	
}
