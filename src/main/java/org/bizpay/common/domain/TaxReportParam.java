package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaxReportParam {
	private String bizCode;
	private String paymentType;
	private String dateStart; // 검색시작일
	private String dateEnd; // 검색종료일
	private String mberName;
	private String bizNum;
	private String usid;
}
