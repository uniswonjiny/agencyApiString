package org.bizpay.mapper;

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
import org.bizpay.domain.LimitInfo;
import org.bizpay.domain.common.SameCardList;

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
	// 계좌이체조정 지급, 미지급처리
	public int acnutTransfrYn(List<AcnutTransfrYnParam> param) throws Exception;
	// 제한 금액 조회
	public LimitInfo limitInfo(String mberCode) throws Exception;
	// 동일 카드 결제 확인용
	public List<String> sameCardList(SameCardList param) throws Exception;
	// 영수증번호 획득및 영수증번호 자동 업데이트동시 실행
	public void propRciptNO(HashMap<String, Object> pam)throws Exception;
}
