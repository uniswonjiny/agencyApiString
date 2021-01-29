package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AgencyCode {
	private String code;// "DISTRIBUTOR"
	private String	codeNm;// "총판"
	private int	sn; // 32
}
