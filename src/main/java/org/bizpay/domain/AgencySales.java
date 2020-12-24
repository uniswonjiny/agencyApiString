package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AgencySales {
	private String bizType;
	private String cmpnm;
	private int cnt;
	private String companyName;
	private int dealerKind;
	private int feeAgency;
	private int feeDealer;
	private int feeDistributor;
	private float feeRate;
	private int key;
	private String mberName;
	private float pymntRate;
	private String t1BizCode ;
	private String t1BizType;
	private String t1Cmpnm;
	private int t1dealerKind;
	private String t2BizCode;
	private String t2BizType;
	private String t2Cmpnm;
	private int t2dealerKind;
	private String t3BizCode;
	private String t3BizType;
	private String t3Cmpnm;
	private int t3dealerKind;
	private String t4;
	private long tot ;
	private String usid;
	private String agencyName; // 대리점명
	private long agencyIncome; // 대리점수익
	private String dealerName; // 딜러이름
	private long dealerIncome; // 딜러수익
	private String totSellPrice;  // 총판매금액;
	private String soleAgencyName; // 총판이름
	private long soleAgencyIncome; // 총판수익
	private int sellerFee; //판매자 수수료
	private long sellerIncome; // 판매자 지급금액 수익

}
