package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
//매출입력
@Getter
@Setter
@ToString
public class DelngInsertParam {
	private String grade; // 세션 인증 권한기능 개발후 삭제한다.
	private String issueCmpnyNm;
	private String usid;
	private String cardNo;
	private String confmDt;
	private String confmTime;
	private String confmNo;
	private double splpc;
	private int instlmtMonth;
	private String delngType; // 카드인지 현금인지 card cash
}
