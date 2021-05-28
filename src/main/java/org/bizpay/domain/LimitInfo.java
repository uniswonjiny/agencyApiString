package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LimitInfo {
	private long limitOne; // 1회 제한금액
	private long limitDay;// 1일 제한금액
	private long limitMonth; // 1달 제한금액
	private long limitYear; // 1년 제한금액
	private int installment; // 할부제한
	private long sumDay; // 하루 결제액
	private long sumMonth; // 한달 결제액
	private long sumYear; // 일년결제액
}
