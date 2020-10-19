package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InqireDealer {
	private String agent;
	private String bankName;
	private String bankSerial;
	private String bankUser;
	private String bizCode;
	private short bizInstallmentMonths;
	private long bizLimitDay;
	private long bizLimitMonth;
	private long bizLimitOne;
	private long bizLimitYear;
	private String bizTelno;
	private String bizType;
	private String bizrno;
	private String bplc;
	private String bprprr;
	private String cmpnm;
	private String creatDt; // 히스토리 생성일시
	private String createDt;
	private String dealerId;
	private int dealerKind;
	private float dealerRate;
	private String dt;
	private String email;
	private String gubun;
	private short installmentMonths;
	private long joinAmt;
	private long limitDay;
	private long limitOne;
	private long limitMonth;
	private long limitYear;
	private String mTelno;
	private int mberCnt;
	private String mberCode;
	private String memberInputYn;
	private float memberRate;
	private String memo;
	private String payBizrno;
	private String payBplc;
	private String payBprprr;
	private String payCmpnm;
	private String payTelno;
	private String pgGb;
	private long pgTrmnlNo; // PG 터미널ID
	private String pgVan;
	private float pymntRate;
	private String recommend;
	private String recommendBizCode;
	private String target;
	private String trgetBizCode;
	private String useAt;
	private String usid;
	private String vanGb;
	private String stopDt; // 거래중지일자
	private String distributor; // 총판
	// 기존소스에는 없지만 쿼리상에 존재하는 항목추가
	private String trmnlNo; // 터미널 항목값에는 없다.
	private String historyMemo;
	private float baroRate; // 익일/바로 수익율
	private float etcRate; // 기타 수익율
}
