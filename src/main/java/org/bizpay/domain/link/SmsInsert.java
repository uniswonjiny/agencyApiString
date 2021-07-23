package org.bizpay.domain.link;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmsInsert {
	private String mberCode;
	private String itId;
	private String itName;
	private String count;
	private String itPrice;
	private String itAddInfo;
	private String itDetailUrl;
	private int totalCount;
	private int totalAmt;
	private String payType;
  	private long smsLinkId;
}
