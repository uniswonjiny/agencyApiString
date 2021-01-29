package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountTrans {
	private String accountNO;//: "469901-04-279499"
	private String bankName;// "국민은행"
	private String confmDt; //"2021-01-25"
	private String depositor;   // "(주)민정운수"
	private double mberCode;// "2008050003"
	private String mberCodeSn; //"001"
	private String payType; //"일반"
	private String pymntYn;// "미지급"
	private double rciptNo;// "2101250001"
	private double splpc;// "63,459"
}
