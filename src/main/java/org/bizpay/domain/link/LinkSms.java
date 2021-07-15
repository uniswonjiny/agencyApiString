package org.bizpay.domain.link;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LinkSms {
	private String itName;
	private String itAddInfo;
	private long itPrice;
	private int mberCode;
	private String itId;
	private String itDetailUrl; 
  	private String mberName;
  	private String mberMobile;
}
