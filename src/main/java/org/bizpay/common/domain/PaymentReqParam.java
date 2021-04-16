package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentReqParam {
	private String ksnetPgIp;
	private int ksnetPgPort;
	private String storeId; // 상점아이디
	private String memberId; // 사용자 아이디
	private String cardNo; // 카드번호
	private String productName; // 상품명
	private String phoneNumber; // 전화번호
	private String KeyIn; // / pKeyInType X(12) KEY-IN유형(K:직접입력,S:리더기사용입력)  
	private int pInterestType;//pInterestType   X( 1)   *일반/무이자구분 1:일반 2:무이자
	private String pTrackII;//-필수- pTrackII  X(40)   *TrackII(KEY-IN방식의 경우 카드번호=유효기간[YYMM]) 
	private String pInstallment ;// -필수- pInstallment  X( 2)   *할부개월수(00:일시불, 03:3개월할부, ...)
	private int amount; // 결제금액
	private int installment ; // 할부-필수- pInstallment  X( 2)   *할부개월수(00:일시불, 03:3개월할부, ...)
	private String cardPassword; // pPasswd  9( 9)   비밀번호앞2자리  
	private String cardResidentNo; //주민번호 뒸자리 pLastIdNum  X(10)  주민번호뒤7자리 또는 사업자번호10자리
}
