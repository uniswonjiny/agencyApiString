package org.bizpay.domain.link;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// sms link 카드 결제 정보
@Getter
@Setter
@ToString
public class SmsCardPayment {
	private String payFinishYn ;
	private String itTotalAmt;
	private String cardNo;
	private String issueCmpnyNm;
    private String confmDt;
	private String instlmtMonth;
	private int splpc;
	private int vat;
	private String mberName;
	private String bizNum;
	private String mberMobile;
	private int rciptNo;
	private int confmNo;
}
