package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DealerInfo {
	private String bizCode;
	private String cmpnm;
	private String trgetBizCode;
	private String grade;
}
