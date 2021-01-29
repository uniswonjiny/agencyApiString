package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SettingAgency {
	private String bizCode;
	private String	cmpnm;// "코스모토(임재수)"
	private String dealerId; //"B000118"
	private int dealerKind; //"33"
}
