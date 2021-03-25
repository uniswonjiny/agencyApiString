package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 추천대리점용
@Getter
@Setter
@ToString
public class AgencySales2 {
	public String t1BizCode;
	public String cmpnm;
	public String bizType;
	public int dealerKind;
	public String usid;
	public double tot;
	public double pymntRate;
	public String mberName;
	public String companyName;
	public String bizNum;
	public double feeRate;
	public String recommendBizCode;
	public int recommendDealerKind;
	public String recommendCmpnm;
	public int cnt;
	public float feeDistributor;
	public float feeAgency;
	public float feeDealer;
	public String t1Cmpnm;
	public String t1BizType;
	public int t1DealerKind;
	public String recommendPBizCode;
	public String t2Cmpnm;
	public String t2BizType;
	public int t2DealerKind;
	public String recommendPDealerKind;
	public String recommendPCmpnm;
	public String t3Cmpnm;
	public String t3BizType;
	public int t3DealerKind;
	public String t4;
	public String t5;
	public String t6;
	
	public String titleCmpnm; // title 대리점
	public String titleDealer; // title 딜러
	public double recommendAgencyFee;// 추천대리점 수익 
	public double recommendDealerFee;// 추천대리점딜러수익
	public double recommendRate; // 추천 수수료율
	
	
	
	
}
