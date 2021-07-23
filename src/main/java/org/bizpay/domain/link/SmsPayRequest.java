package org.bizpay.domain.link;

/*
 * sms 결제용 결제요청 파라미터
 * 추후 링크등과 결합을 할지 아니면 따로 짜로 갈지 생각하자
 * */

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmsPayRequest {
	private long id;
	private String itId; // 상품코드
	private String mberCode; // 판매자 멤버코드
	private String recipient; // 배송받는사람
	private String mobilePhone; // 배송받을사람 핸드폰번호
	private String addrInfo; // 배송지 주소 
	private String addrDetailInfo; //배송지 상세주소
	private String zonecode; // 배송지 우편번호
	private String message; // 배송지용 메세지
	private int step; // 기존것에 있던 단계 ! 나중에 처리하자
	private long rciptNo; // 비즈페이사용 영수증번호
	private String payType; // 결제타입
	private String count;// 전체 주문갯수 link에서 사용한다.
	private long totAmt; // 전체 결제금액
	private String cardNumber;// 결제요청 카드번호
	private String expiration; // 카드 유효기간
	private String installment; // 할부기간
	private String pidNum; // 생년 법인번호 - 수기 인증인경우
	private String passwd; // 카드번호 - 수기 인증인경우
	private String cardEmail; // 카드결제한 사람 이메일
	private String cardMobilePhone;// 카드결제한 사람 핸드폰 번호
	private String finshYn;// PAY_FINISH_YN;
  	private int installmentMonths;
  	private String sugiCertification;
  	private String payFinishYn;
}
