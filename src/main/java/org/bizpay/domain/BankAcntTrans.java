package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BankAcntTrans {
	private String accountNo;// "469901-04-279499"
	private String bankFee;// "0"
	private String bankName;// "국민은행"
	private String cnt;// "1"
	private String confmDtSub;// "2021-01-25"
	private String fpayDt;// "2021-01-25"
	private String depositor;// "(주)민정운수"
	private String key;// "1"
	private String payAmount;// "63,459"
	private String payDt;// "2021-01-29"
	private String payType;// "일반"
	private String pymntYn;// "미지급"
}
