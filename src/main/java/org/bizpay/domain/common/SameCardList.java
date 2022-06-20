package org.bizpay.domain.common;
// 동일카드검사

import lombok.Data;
@Data
public class SameCardList {
	private String mberCode;
	private String dateStr;
	private long price;
}
