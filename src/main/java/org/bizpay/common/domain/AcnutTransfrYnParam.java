package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AcnutTransfrYnParam {
	private String pymntYn;
	private String mberCode;
	private String mberCodeSn;
	private String rciptNo;
}
