package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExternalOrderInputParam {
	private String nextUrl;
	private String notiUrl;
	private String pkHash;
	private String status;
	private String orderName;
	private int orderPrice;
	private long seq;
	private String exorderNo;
	private String mberId;
}
