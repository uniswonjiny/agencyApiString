package org.bizpay.domain.link;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 결제정보
 * */
@Getter
@Setter
@ToString
public class PaymentInfo {
	private String type; // 결제유형 card , cash
	private String dateStr; // 결제한 시간
	private String name ; // 결제타입이름 카드이름
	private String paymentNo; // 카드번호등
	private int rciptNo; // 영수증번호
	
}
