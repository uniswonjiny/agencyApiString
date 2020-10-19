package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InqireDelngSum {
	private long sum;
	private long cardSum;
	private int cardCount;
	private long cashSum;
	private int cashCount;
}
