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
	private String indutyId;// 로그인 판매자 아이디
	public String useAt; // 거래구분
	public String bizNum; // 사업자번호
	public String memberBizeCode; // 로그인한 비즈코드
	public String memberMberCode; // 로그인한 사용자코드 -- 로그인 인증키기능 강화 -> 인증키정보로 자동으로 사용자 정보 추출 기능개발 
	public String bizrno; // 기존소스에 bizNum bizrno 사업자 번호로 혼용되어 사용됨 주의!!!!!
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
	public String password;// 패스워드
	
	public String prevTarget;
	public String prevDealerKind;
	public int cDealerKind; // 바꾸고자 하는 대리점 구분
	public int pDearlerKind;// 현재 대리점 구분
	public String creatMberCode; // 입력하고 있는 멤버코드
	
}
