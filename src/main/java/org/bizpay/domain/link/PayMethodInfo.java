package org.bizpay.domain.link;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 결제 정보 -- 일단은 카드 정보만 추후 다른 결제 수단 파라미터를 추가한다.
@Getter
@Setter
@ToString
public class PayMethodInfo {
	// 카드결제정보
	private String installment; //  할부 개월수 (문자인 이유는 "00" "01" 이런 형태)
	private String cardNumber; // 카드 번호
	private String expiration; // 만료 기간
	private long price; // 총 결제 금액
	private String pidNum; // 주민번호 사업자번호 - 수기인증필요시
	private String passwd; // 카드비밀번호 - 수기인증필요시
}
