package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//매출취소사유
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
	private String grade; // 세션 인증 권한기능 개발후 삭제한다. 
	
}   
