package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AtmConfig {
	private String msg;
	private String enableYn;
	private String gbCode;	
}
