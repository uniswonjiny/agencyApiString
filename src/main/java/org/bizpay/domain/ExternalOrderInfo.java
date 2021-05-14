package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExternalOrderInfo {
	private long seq; // 주문번호 겸 시퀀스
	private String mberId; // 주문주체 외부요청자
	private String mberName; // 주문주체 외부요청자
	private String name; //  주문명
	private int price; // 가격
	private String createDt; // 요청일자
	private String updateDt; // 수정일자
	private String status; // 주문상태
}
