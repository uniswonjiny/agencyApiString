package org.bizpay.domain.link;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 판매자 정보
@Getter
@Setter
@ToString
public class SellerInfo {
	private String mberCode; // 판매자 멤버코드
  	private String mberName; // 판매자 이름
  	private String mberMobile; // 판매자 전화번호
  	private String companyName; // 판매자 상호명
  	private int installmentMonths; // 할부가능개월수 
  	private String sugiCertification; // 수기 인증결제 값
  	private String smslinkMemo;
  	private String custPhone; // 구매자 연락처 배송지 연락처와는 다르다.
}
