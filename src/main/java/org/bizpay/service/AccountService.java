package org.bizpay.service;

import java.util.HashMap;
import java.util.List;

import org.bizpay.common.domain.AccountExcelParam;
import org.bizpay.common.domain.AccountListParam;
import org.bizpay.common.domain.AccountTransParam;
import org.bizpay.common.domain.AcnutTransfrYnParam;
import org.bizpay.common.domain.BankAcntTransParam;
import org.bizpay.domain.AccountExcel;
import org.bizpay.domain.AccountInOut;
import org.bizpay.domain.AccountTrans;
import org.bizpay.domain.BankAcntTrans;

public interface AccountService {
	// 입출력내역목록
	public List<AccountInOut> AccountInList(AccountListParam param)  throws Exception;
	// 정산용엑셀
	public List<AccountExcel> accountExcelList(AccountExcelParam param) throws Exception;
	// 계좌이체조정목록
	public List<AccountTrans> transferList(AccountTransParam param) throws Exception;
	// 은행계좌이체
	public List<BankAcntTrans> bankAcntTransList(BankAcntTransParam param) throws Exception;
	// 출금정지설정
	public int inOutSetting(HashMap<String, Object> map) throws Exception;
	// 출금정지상태
	public HashMap<String, Object> inOutSettingInfo() throws Exception;
	// 계좌이체조정 지급, 미지급처리
	public int acnutTransfrYn(List<AcnutTransfrYnParam> param) throws Exception;
}
