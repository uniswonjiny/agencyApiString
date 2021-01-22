package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountInOut {
    public String cmpnm;
    public String indutyId ;
    public String mberName;
    public String companyName;
    public String bizNum;
    public int mberCode;
	public double inoutNo;
	public String inoutCode;
	public String reqAmt;
	public double balance;
	public String reqDt;
	public int charge;
	public String reqResult;
	public String bank;
	public String account;
	public String depositor;
	public String bigo;
	public String bizCode;
	public String in_dailySalesTot_amt;
	public String in_dailySalesFee_per;
	public String in_dailySalesFee_amt;
	public String in_dailySalesDt;
	public String in_dailySalesCount;
	public double salesTotAmt;
	public float salesFeePer;
	public double salesFeeAmt;
	public String salesDt;
	public String salesTime;
	public double salesRciptNo;
}
