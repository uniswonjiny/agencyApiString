package org.bizpay.domain.common;
// 동일카드검사

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SameCardList {
	private String mberCode;
	private String dateStr;
	private long price;
}
