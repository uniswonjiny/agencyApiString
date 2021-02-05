package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PgFee {
	private int idx;
	private float defaultFee; // 기본 수수료율
	private float fee01; // 익일 수수료율
	private float fee02; // 5일 수수료율
	private float fee03;
	private String pgName; // pg 이름
}
