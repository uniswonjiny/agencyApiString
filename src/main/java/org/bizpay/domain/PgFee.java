package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PgFee {
	private int idx;
	private float defaultFee;
	private float fee01;
	private float fee02;
	private float fee03;
	private String pgName;
}
