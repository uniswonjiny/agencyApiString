package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 판매자매출
@Getter
@Setter
@ToString
public class SellerSummary {
	public double cardCnt;
	public double cardTot;
	public double cashCnt;
	public double cashTot;
	public String cmpnm;
	public String companyName;
	public String bizNum;
	public double feeRate;
	public String kpayType;
	public String mberName;
	public String payType;
	public double tot;
	public double totCnt;
	public String usid;
	public double fee; // 수수료
	public double pay; // 지급금액

}
