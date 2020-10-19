package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InqireAtmSum {
	private int inCnt;
	private long inSum;
	private int outCnt;
	private long outSum ;
	private int cancelCnt ;
	private long cancelSum ;
	private int cnt;
}
