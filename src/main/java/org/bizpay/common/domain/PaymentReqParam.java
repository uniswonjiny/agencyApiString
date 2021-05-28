package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentReqParam {
	private String memberId;
	private String cardNo; // 카드번호
	private String expiration; // 카드유호기간
	private String installment ;// -필수- pInstallment  X( 2)   *할부개월수(00:일시불, 03:3개월할부, ...)
	private long amount; // 결제금액
	private String exorderNo; // 외부결제정보
	
	private String passwd; // pPasswd  9( 9)   비밀번호앞2자리  
	private String cardPNo; //주민번호 뒸자리 pLastIdNum  X(10)  주민번호앞6자리 또는 사업자번호10자리 

	private String orderName; // 상품명
	private String phoneNumber; // 전화번호
	private String pidNum; // 주민번호나 사업자번호?
	private String pTrackII;//-필수- pTrackII  X(40)   *TrackII(KEY-IN방식의 경우 카드번호=유효기간[YYMM]) 
	private String email; // 이메일
	private int mberCode; // 사용자코드
	private String rciptNo; // 영수증번호
	private String ksnetRcipt; // ksnet 에보내는 영수증번호  사용자아이디 + 영수증번호
	private String status; // 결제취소 결제요청등
}
