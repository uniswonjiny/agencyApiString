package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransByDate {
	private String confmDt; // 날짜
	private String delngSecode; // 거래형태  CARD_CNCL - 카드취소 CARD_ISSUE - 카드거래 CASH_RCIPT_ISSUE - 현금거래 CASH_RCIPT_CNCL - 현금취소
	private int totSum; // 금액합계
	private int totCnt; // 전체건수(거래량)
}
