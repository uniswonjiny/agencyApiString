package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DelngCancelParam {
	private String usid;
	private String cancelDate;
	private String cancelReason;
	private int mberCode;
	private int mberCodeSn;
	private String rciptNo;
	
	private String pgRciptNo;
	private String tId;
	private String payType;
	private String confmDt;
	private String delngSeCode;
	
}   
