package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DealerRegInfo {
	private String dealerId;
	private String usid;
	private int dealerKind;
	private String dealerKindName;
	private String cmpnm;
	private String bplc;
	private int joinAmt;
	private String bizrno;
	private String authorCode;
	private String bizType;
	private String bizTelno;
	private String mTelno;
	private String email;
	private String bankName;
	private String bankSerial;
	private String bankUser;
	private float dealerRate;
	private float memberRate;
	private int memberInputYn;
	private String useAt;
	private String memo;
	private String historyMemo;
	private String trmnlNo;
	private String pgTrmnlNo;
	private String pgVan;
	private String vanGb;
	private String dt;
	private String payBprprr;
	private String payTelno;
	private String payCmpnm;
	private String payBizrno;
	private String payBplc;
	private int limitOne; 
	private int limitDay;
	private int limitMonth;
	private int limitYear;
	private int installmentMonths;
	private int bizLimitOne;
	private int bizLimitDay;
	private int bizLimitMonth;
	private int bizLimitYear;
	private int bizInstallmentMonths;
	private String recommendBizCode;
	private String trgetBizCode;
	private String bizCode;
	private String bprprr;
}
