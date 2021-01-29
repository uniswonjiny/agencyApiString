package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Guarant {
	private String usid;
	private String mberName;
	private String mberMobile;
	private String paymentDate;
	private String paymentAmount;
	private String limitOne;
	private String limitDay;
	private String limitMonth;
	private String limitYear;
	private String useAt;
	private String mberCode;
	private String nextDate;
	private String smsSendDate;
}
