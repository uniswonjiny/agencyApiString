package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AgencyManageParam {
	public String cmpnm; // 상호
	public String dealerId; // 대리점 아아디
	public String useAt; // 거래구분
	public String bizNum; // 사업자번호
	public String memberBizeCode; // 로그인한 비즈코드
	
	public String bizrno;
	public String trmnlNo;
	public String pgTrmnlNo;
	public String pgVan;
	public String payBprprr;
	public String payTelno;
	public String payCmpnm;
	public String payBizrno;
	public String payBplc;
	public String mTelno;
	public String email;
	public String bankName;
	public String bankSerial;
	public String createDt;
	public String bankUser;
	public float dealerRate;
	public String memberInputYn;
	public float memberRate;
	public String memo;
	public String historyMemo;
	public double joinAmt;
	public double limitOne;
	public double limitDay;
	public double limitMonth;
	public double limitYear;
	public int installmentMonths;
	public double bizLimitOne;
	public double bizLimitDay;
	public double bizLimitMonth;
	public double bizLimitYear;
	public int bizInstallmentMonths;
	public String pgGb;
	public String vanGb;
	public String bizType;
	public String usid;
	public String mberCode;
	public String bizCode;
	public String bprprr;
	public String bplc;
	public String bizTelno;
	public double pymntRate;
	public String dt;
	public String trgetBizCode;
	public String recommendBizCode;
	public float baroRate;
	public float etcRate;
	public String dealerKind;
	public String gubun;
	public String target;
	public String recommend;
	public String agent;
	public String distributor;
	public int mberCnt;
	public String creatDt;
	
	public String prevTarget;
	public String prevDealerKind;
	public int cDealerKind; // 바꾸고자 하는 대리점 구분
	public int pDearlerKind;// 현재 대리점 구분
	
	
	
}
