package org.bizpay.domain.link;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Destination {
	private int mberCode;
	private long rciptNo;
	private String recipient;
	private String mobilePhone;
	private String address;
	private String message;
}
