package org.bizpay.domain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InqireAtm {
	private String cmpnm; 
	private String indutyId;
	private String mberName;
	private long mberCode;
	private long inoutNo;
	private String inoutCode;
	private long reqAmt;
	private long balance;
	private String reqDt;
	private int charge;
	private String reqResult;
	private String bank;
	private String account;
	private String depositor;
	private String bigo;
	private String bizCode;
	private long inDailySalesTotAmt;
	private float inDailySalesFeePer;
	private long inDailySalesFeeAmt;
	private String inDailySalesDt;
	private int inDailySalesCount;
	private long salesTotAmt;
	private float salesFeePer;
	private long salesFeeAmt;
	private String salesDt;
	private String salesTime;
	private long salesRciptNo;
	private String companyName;
	private String bizNum;
}
